// $ANTLR 3.5.2 F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g 2015-08-04 11:03:41

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
		"INNER", "INT_NUMERAL", "JOIN", "LEFT", "LINE_COMMENT", "LPAREN", "MAX", 
		"MIN", "NAMED_PARAMETER", "OR", "ORDER", "OUTER", "RPAREN", "RUSSIAN_SYMBOLS", 
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
		"'LIKE'", "'LOCATE('", "'LOWER('", "'MEMBER'", "'MINUTE'", "'MOD('", "'MONTH'", 
		"'NEW'", "'NOT'", "'NOW'", "'NULL'", "'NULLIF('", "'OBJECT'", "'OF'", 
		"'ON'", "'SECOND'", "'SELECT'", "'SET'", "'SIZE('", "'SOME'", "'SQRT('", 
		"'SUBSTRING('", "'THEN'", "'TRAILING'", "'TREAT('", "'TRIM('", "'TYPE'", 
		"'UPDATE'", "'UPPER('", "'VALUE('", "'WHEN'", "'WHERE'", "'YEAR'", "'false'", 
		"'true'", "'}'"
	};
	public static final int EOF=-1;
	public static final int T__52=52;
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
	public static final int LPAREN=21;
	public static final int MAX=22;
	public static final int MIN=23;
	public static final int NAMED_PARAMETER=24;
	public static final int OR=25;
	public static final int ORDER=26;
	public static final int OUTER=27;
	public static final int RPAREN=28;
	public static final int RUSSIAN_SYMBOLS=29;
	public static final int STRING_LITERAL=30;
	public static final int SUM=31;
	public static final int TRIM_CHARACTER=32;
	public static final int T_AGGREGATE_EXPR=33;
	public static final int T_COLLECTION_MEMBER=34;
	public static final int T_CONDITION=35;
	public static final int T_GROUP_BY=36;
	public static final int T_ID_VAR=37;
	public static final int T_JOIN_VAR=38;
	public static final int T_ORDER_BY=39;
	public static final int T_ORDER_BY_FIELD=40;
	public static final int T_PARAMETER=41;
	public static final int T_QUERY=42;
	public static final int T_SELECTED_ENTITY=43;
	public static final int T_SELECTED_FIELD=44;
	public static final int T_SELECTED_ITEM=45;
	public static final int T_SELECTED_ITEMS=46;
	public static final int T_SIMPLE_CONDITION=47;
	public static final int T_SOURCE=48;
	public static final int T_SOURCES=49;
	public static final int WORD=50;
	public static final int WS=51;

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
	@Override public String getGrammarFileName() { return "F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g"; }


	public static class ql_statement_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "ql_statement"
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:75:1: ql_statement : select_statement ;
	public final JPA2Parser.ql_statement_return ql_statement() throws RecognitionException {
		JPA2Parser.ql_statement_return retval = new JPA2Parser.ql_statement_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope select_statement1 =null;


		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:76:5: ( select_statement )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:76:7: select_statement
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_select_statement_in_ql_statement416);
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:78:1: select_statement : sl= 'SELECT' select_clause from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? -> ^( T_QUERY[$sl] ( select_clause )? from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? ) ;
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
		RewriteRuleSubtreeStream stream_select_clause=new RewriteRuleSubtreeStream(adaptor,"rule select_clause");
		RewriteRuleSubtreeStream stream_groupby_clause=new RewriteRuleSubtreeStream(adaptor,"rule groupby_clause");
		RewriteRuleSubtreeStream stream_from_clause=new RewriteRuleSubtreeStream(adaptor,"rule from_clause");
		RewriteRuleSubtreeStream stream_having_clause=new RewriteRuleSubtreeStream(adaptor,"rule having_clause");
		RewriteRuleSubtreeStream stream_where_clause=new RewriteRuleSubtreeStream(adaptor,"rule where_clause");
		RewriteRuleSubtreeStream stream_orderby_clause=new RewriteRuleSubtreeStream(adaptor,"rule orderby_clause");

		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:79:6: (sl= 'SELECT' select_clause from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? -> ^( T_QUERY[$sl] ( select_clause )? from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? ) )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:79:8: sl= 'SELECT' select_clause from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )?
			{
			sl=(Token)match(input,117,FOLLOW_117_in_select_statement431); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_117.add(sl);

			pushFollow(FOLLOW_select_clause_in_select_statement433);
			select_clause2=select_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_select_clause.add(select_clause2.getTree());
			pushFollow(FOLLOW_from_clause_in_select_statement435);
			from_clause3=from_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_from_clause.add(from_clause3.getTree());
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:79:46: ( where_clause )?
			int alt1=2;
			int LA1_0 = input.LA(1);
			if ( (LA1_0==132) ) {
				alt1=1;
			}
			switch (alt1) {
				case 1 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:79:47: where_clause
					{
					pushFollow(FOLLOW_where_clause_in_select_statement438);
					where_clause4=where_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_where_clause.add(where_clause4.getTree());
					}
					break;

			}

			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:79:62: ( groupby_clause )?
			int alt2=2;
			int LA2_0 = input.LA(1);
			if ( (LA2_0==GROUP) ) {
				alt2=1;
			}
			switch (alt2) {
				case 1 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:79:63: groupby_clause
					{
					pushFollow(FOLLOW_groupby_clause_in_select_statement443);
					groupby_clause5=groupby_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_groupby_clause.add(groupby_clause5.getTree());
					}
					break;

			}

			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:79:80: ( having_clause )?
			int alt3=2;
			int LA3_0 = input.LA(1);
			if ( (LA3_0==HAVING) ) {
				alt3=1;
			}
			switch (alt3) {
				case 1 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:79:81: having_clause
					{
					pushFollow(FOLLOW_having_clause_in_select_statement448);
					having_clause6=having_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_having_clause.add(having_clause6.getTree());
					}
					break;

			}

			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:79:97: ( orderby_clause )?
			int alt4=2;
			int LA4_0 = input.LA(1);
			if ( (LA4_0==ORDER) ) {
				alt4=1;
			}
			switch (alt4) {
				case 1 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:79:98: orderby_clause
					{
					pushFollow(FOLLOW_orderby_clause_in_select_statement453);
					orderby_clause7=orderby_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_orderby_clause.add(orderby_clause7.getTree());
					}
					break;

			}

			// AST REWRITE
			// elements: where_clause, having_clause, from_clause, select_clause, orderby_clause, groupby_clause
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 80:6: -> ^( T_QUERY[$sl] ( select_clause )? from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? )
			{
				// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:80:9: ^( T_QUERY[$sl] ( select_clause )? from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ( orderby_clause )? )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new QueryNode(T_QUERY, sl), root_1);
				// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:80:35: ( select_clause )?
				if ( stream_select_clause.hasNext() ) {
					adaptor.addChild(root_1, stream_select_clause.nextTree());
				}
				stream_select_clause.reset();

				adaptor.addChild(root_1, stream_from_clause.nextTree());
				// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:80:64: ( where_clause )?
				if ( stream_where_clause.hasNext() ) {
					adaptor.addChild(root_1, stream_where_clause.nextTree());
				}
				stream_where_clause.reset();

				// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:80:80: ( groupby_clause )?
				if ( stream_groupby_clause.hasNext() ) {
					adaptor.addChild(root_1, stream_groupby_clause.nextTree());
				}
				stream_groupby_clause.reset();

				// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:80:98: ( having_clause )?
				if ( stream_having_clause.hasNext() ) {
					adaptor.addChild(root_1, stream_having_clause.nextTree());
				}
				stream_having_clause.reset();

				// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:80:115: ( orderby_clause )?
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:82:1: update_statement : 'UPDATE' update_clause ( where_clause )? ;
	public final JPA2Parser.update_statement_return update_statement() throws RecognitionException {
		JPA2Parser.update_statement_return retval = new JPA2Parser.update_statement_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal8=null;
		ParserRuleReturnScope update_clause9 =null;
		ParserRuleReturnScope where_clause10 =null;

		Object string_literal8_tree=null;

		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:83:5: ( 'UPDATE' update_clause ( where_clause )? )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:83:7: 'UPDATE' update_clause ( where_clause )?
			{
			root_0 = (Object)adaptor.nil();


			string_literal8=(Token)match(input,128,FOLLOW_128_in_update_statement509); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal8_tree = (Object)adaptor.create(string_literal8);
			adaptor.addChild(root_0, string_literal8_tree);
			}

			pushFollow(FOLLOW_update_clause_in_update_statement511);
			update_clause9=update_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, update_clause9.getTree());

			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:83:30: ( where_clause )?
			int alt5=2;
			int LA5_0 = input.LA(1);
			if ( (LA5_0==132) ) {
				alt5=1;
			}
			switch (alt5) {
				case 1 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:83:31: where_clause
					{
					pushFollow(FOLLOW_where_clause_in_update_statement514);
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:84:1: delete_statement : 'DELETE' 'FROM' delete_clause ( where_clause )? ;
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
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:85:5: ( 'DELETE' 'FROM' delete_clause ( where_clause )? )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:85:7: 'DELETE' 'FROM' delete_clause ( where_clause )?
			{
			root_0 = (Object)adaptor.nil();


			string_literal11=(Token)match(input,85,FOLLOW_85_in_delete_statement527); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal11_tree = (Object)adaptor.create(string_literal11);
			adaptor.addChild(root_0, string_literal11_tree);
			}

			string_literal12=(Token)match(input,92,FOLLOW_92_in_delete_statement529); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal12_tree = (Object)adaptor.create(string_literal12);
			adaptor.addChild(root_0, string_literal12_tree);
			}

			pushFollow(FOLLOW_delete_clause_in_delete_statement531);
			delete_clause13=delete_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, delete_clause13.getTree());

			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:85:37: ( where_clause )?
			int alt6=2;
			int LA6_0 = input.LA(1);
			if ( (LA6_0==132) ) {
				alt6=1;
			}
			switch (alt6) {
				case 1 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:85:38: where_clause
					{
					pushFollow(FOLLOW_where_clause_in_delete_statement534);
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:87:1: from_clause : fr= 'FROM' identification_variable_declaration ( ',' identification_variable_declaration_or_collection_member_declaration )* -> ^( T_SOURCES[$fr] identification_variable_declaration ( identification_variable_declaration_or_collection_member_declaration )* ) ;
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
		RewriteRuleTokenStream stream_92=new RewriteRuleTokenStream(adaptor,"token 92");
		RewriteRuleTokenStream stream_55=new RewriteRuleTokenStream(adaptor,"token 55");
		RewriteRuleSubtreeStream stream_identification_variable_declaration=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable_declaration");
		RewriteRuleSubtreeStream stream_identification_variable_declaration_or_collection_member_declaration=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable_declaration_or_collection_member_declaration");

		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:88:6: (fr= 'FROM' identification_variable_declaration ( ',' identification_variable_declaration_or_collection_member_declaration )* -> ^( T_SOURCES[$fr] identification_variable_declaration ( identification_variable_declaration_or_collection_member_declaration )* ) )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:88:8: fr= 'FROM' identification_variable_declaration ( ',' identification_variable_declaration_or_collection_member_declaration )*
			{
			fr=(Token)match(input,92,FOLLOW_92_in_from_clause551); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_92.add(fr);

			pushFollow(FOLLOW_identification_variable_declaration_in_from_clause553);
			identification_variable_declaration15=identification_variable_declaration();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_identification_variable_declaration.add(identification_variable_declaration15.getTree());
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:88:54: ( ',' identification_variable_declaration_or_collection_member_declaration )*
			loop7:
			while (true) {
				int alt7=2;
				int LA7_0 = input.LA(1);
				if ( (LA7_0==55) ) {
					alt7=1;
				}

				switch (alt7) {
				case 1 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:88:55: ',' identification_variable_declaration_or_collection_member_declaration
					{
					char_literal16=(Token)match(input,55,FOLLOW_55_in_from_clause556); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_55.add(char_literal16);

					pushFollow(FOLLOW_identification_variable_declaration_or_collection_member_declaration_in_from_clause558);
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
			// 89:6: -> ^( T_SOURCES[$fr] identification_variable_declaration ( identification_variable_declaration_or_collection_member_declaration )* )
			{
				// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:89:9: ^( T_SOURCES[$fr] identification_variable_declaration ( identification_variable_declaration_or_collection_member_declaration )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new FromNode(T_SOURCES, fr), root_1);
				adaptor.addChild(root_1, stream_identification_variable_declaration.nextTree());
				// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:89:72: ( identification_variable_declaration_or_collection_member_declaration )*
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:90:1: identification_variable_declaration_or_collection_member_declaration : ( identification_variable_declaration | collection_member_declaration -> ^( T_SOURCE collection_member_declaration ) );
	public final JPA2Parser.identification_variable_declaration_or_collection_member_declaration_return identification_variable_declaration_or_collection_member_declaration() throws RecognitionException {
		JPA2Parser.identification_variable_declaration_or_collection_member_declaration_return retval = new JPA2Parser.identification_variable_declaration_or_collection_member_declaration_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope identification_variable_declaration18 =null;
		ParserRuleReturnScope collection_member_declaration19 =null;

		RewriteRuleSubtreeStream stream_collection_member_declaration=new RewriteRuleSubtreeStream(adaptor,"rule collection_member_declaration");

		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:91:6: ( identification_variable_declaration | collection_member_declaration -> ^( T_SOURCE collection_member_declaration ) )
			int alt8=2;
			int LA8_0 = input.LA(1);
			if ( (LA8_0==WORD) ) {
				alt8=1;
			}
			else if ( (LA8_0==95) ) {
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
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:91:8: identification_variable_declaration
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_declaration_in_identification_variable_declaration_or_collection_member_declaration592);
					identification_variable_declaration18=identification_variable_declaration();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable_declaration18.getTree());

					}
					break;
				case 2 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:92:8: collection_member_declaration
					{
					pushFollow(FOLLOW_collection_member_declaration_in_identification_variable_declaration_or_collection_member_declaration601);
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
					// 92:38: -> ^( T_SOURCE collection_member_declaration )
					{
						// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:92:41: ^( T_SOURCE collection_member_declaration )
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:94:1: identification_variable_declaration : range_variable_declaration ( joined_clause )* -> ^( T_SOURCE range_variable_declaration ( joined_clause )* ) ;
	public final JPA2Parser.identification_variable_declaration_return identification_variable_declaration() throws RecognitionException {
		JPA2Parser.identification_variable_declaration_return retval = new JPA2Parser.identification_variable_declaration_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope range_variable_declaration20 =null;
		ParserRuleReturnScope joined_clause21 =null;

		RewriteRuleSubtreeStream stream_joined_clause=new RewriteRuleSubtreeStream(adaptor,"rule joined_clause");
		RewriteRuleSubtreeStream stream_range_variable_declaration=new RewriteRuleSubtreeStream(adaptor,"rule range_variable_declaration");

		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:95:6: ( range_variable_declaration ( joined_clause )* -> ^( T_SOURCE range_variable_declaration ( joined_clause )* ) )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:95:8: range_variable_declaration ( joined_clause )*
			{
			pushFollow(FOLLOW_range_variable_declaration_in_identification_variable_declaration625);
			range_variable_declaration20=range_variable_declaration();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_range_variable_declaration.add(range_variable_declaration20.getTree());
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:95:35: ( joined_clause )*
			loop9:
			while (true) {
				int alt9=2;
				int LA9_0 = input.LA(1);
				if ( (LA9_0==INNER||(LA9_0 >= JOIN && LA9_0 <= LEFT)) ) {
					alt9=1;
				}

				switch (alt9) {
				case 1 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:95:35: joined_clause
					{
					pushFollow(FOLLOW_joined_clause_in_identification_variable_declaration627);
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
			// 96:6: -> ^( T_SOURCE range_variable_declaration ( joined_clause )* )
			{
				// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:96:9: ^( T_SOURCE range_variable_declaration ( joined_clause )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new SelectionSourceNode(T_SOURCE), root_1);
				adaptor.addChild(root_1, stream_range_variable_declaration.nextTree());
				// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:96:68: ( joined_clause )*
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:97:1: joined_clause : ( join | fetch_join );
	public final JPA2Parser.joined_clause_return joined_clause() throws RecognitionException {
		JPA2Parser.joined_clause_return retval = new JPA2Parser.joined_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope join22 =null;
		ParserRuleReturnScope fetch_join23 =null;


		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:97:15: ( join | fetch_join )
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
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:97:17: join
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_join_in_joined_clause654);
					join22=join();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, join22.getTree());

					}
					break;
				case 2 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:97:24: fetch_join
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_fetch_join_in_joined_clause658);
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:98:1: range_variable_declaration : entity_name ( 'AS' )? identification_variable -> ^( T_ID_VAR[$identification_variable.text] entity_name ) ;
	public final JPA2Parser.range_variable_declaration_return range_variable_declaration() throws RecognitionException {
		JPA2Parser.range_variable_declaration_return retval = new JPA2Parser.range_variable_declaration_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal25=null;
		ParserRuleReturnScope entity_name24 =null;
		ParserRuleReturnScope identification_variable26 =null;

		Object string_literal25_tree=null;
		RewriteRuleTokenStream stream_75=new RewriteRuleTokenStream(adaptor,"token 75");
		RewriteRuleSubtreeStream stream_identification_variable=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable");
		RewriteRuleSubtreeStream stream_entity_name=new RewriteRuleSubtreeStream(adaptor,"rule entity_name");

		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:99:6: ( entity_name ( 'AS' )? identification_variable -> ^( T_ID_VAR[$identification_variable.text] entity_name ) )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:99:8: entity_name ( 'AS' )? identification_variable
			{
			pushFollow(FOLLOW_entity_name_in_range_variable_declaration670);
			entity_name24=entity_name();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_entity_name.add(entity_name24.getTree());
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:99:20: ( 'AS' )?
			int alt11=2;
			int LA11_0 = input.LA(1);
			if ( (LA11_0==75) ) {
				alt11=1;
			}
			switch (alt11) {
				case 1 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:99:21: 'AS'
					{
					string_literal25=(Token)match(input,75,FOLLOW_75_in_range_variable_declaration673); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_75.add(string_literal25);

					}
					break;

			}

			pushFollow(FOLLOW_identification_variable_in_range_variable_declaration677);
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
			// 100:6: -> ^( T_ID_VAR[$identification_variable.text] entity_name )
			{
				// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:100:9: ^( T_ID_VAR[$identification_variable.text] entity_name )
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:101:1: join : join_spec join_association_path_expression ( 'AS' )? identification_variable ( join_condition )? -> ^( T_JOIN_VAR[$join_spec.text, $identification_variable.text] join_association_path_expression ) ;
	public final JPA2Parser.join_return join() throws RecognitionException {
		JPA2Parser.join_return retval = new JPA2Parser.join_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal29=null;
		ParserRuleReturnScope join_spec27 =null;
		ParserRuleReturnScope join_association_path_expression28 =null;
		ParserRuleReturnScope identification_variable30 =null;
		ParserRuleReturnScope join_condition31 =null;

		Object string_literal29_tree=null;
		RewriteRuleTokenStream stream_75=new RewriteRuleTokenStream(adaptor,"token 75");
		RewriteRuleSubtreeStream stream_identification_variable=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable");
		RewriteRuleSubtreeStream stream_join_association_path_expression=new RewriteRuleSubtreeStream(adaptor,"rule join_association_path_expression");
		RewriteRuleSubtreeStream stream_join_spec=new RewriteRuleSubtreeStream(adaptor,"rule join_spec");
		RewriteRuleSubtreeStream stream_join_condition=new RewriteRuleSubtreeStream(adaptor,"rule join_condition");

		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:102:6: ( join_spec join_association_path_expression ( 'AS' )? identification_variable ( join_condition )? -> ^( T_JOIN_VAR[$join_spec.text, $identification_variable.text] join_association_path_expression ) )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:102:8: join_spec join_association_path_expression ( 'AS' )? identification_variable ( join_condition )?
			{
			pushFollow(FOLLOW_join_spec_in_join706);
			join_spec27=join_spec();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_join_spec.add(join_spec27.getTree());
			pushFollow(FOLLOW_join_association_path_expression_in_join708);
			join_association_path_expression28=join_association_path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_join_association_path_expression.add(join_association_path_expression28.getTree());
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:102:51: ( 'AS' )?
			int alt12=2;
			int LA12_0 = input.LA(1);
			if ( (LA12_0==75) ) {
				alt12=1;
			}
			switch (alt12) {
				case 1 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:102:52: 'AS'
					{
					string_literal29=(Token)match(input,75,FOLLOW_75_in_join711); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_75.add(string_literal29);

					}
					break;

			}

			pushFollow(FOLLOW_identification_variable_in_join715);
			identification_variable30=identification_variable();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_identification_variable.add(identification_variable30.getTree());
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:102:83: ( join_condition )?
			int alt13=2;
			int LA13_0 = input.LA(1);
			if ( (LA13_0==115) ) {
				alt13=1;
			}
			switch (alt13) {
				case 1 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:102:84: join_condition
					{
					pushFollow(FOLLOW_join_condition_in_join718);
					join_condition31=join_condition();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_join_condition.add(join_condition31.getTree());
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
			// 103:6: -> ^( T_JOIN_VAR[$join_spec.text, $identification_variable.text] join_association_path_expression )
			{
				// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:103:9: ^( T_JOIN_VAR[$join_spec.text, $identification_variable.text] join_association_path_expression )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new JoinVariableNode(T_JOIN_VAR, (join_spec27!=null?input.toString(join_spec27.start,join_spec27.stop):null), (identification_variable30!=null?input.toString(identification_variable30.start,identification_variable30.stop):null)), root_1);
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:104:1: fetch_join : join_spec 'FETCH' join_association_path_expression ;
	public final JPA2Parser.fetch_join_return fetch_join() throws RecognitionException {
		JPA2Parser.fetch_join_return retval = new JPA2Parser.fetch_join_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal33=null;
		ParserRuleReturnScope join_spec32 =null;
		ParserRuleReturnScope join_association_path_expression34 =null;

		Object string_literal33_tree=null;

		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:105:6: ( join_spec 'FETCH' join_association_path_expression )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:105:8: join_spec 'FETCH' join_association_path_expression
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_join_spec_in_fetch_join749);
			join_spec32=join_spec();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, join_spec32.getTree());

			string_literal33=(Token)match(input,FETCH,FOLLOW_FETCH_in_fetch_join751); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal33_tree = (Object)adaptor.create(string_literal33);
			adaptor.addChild(root_0, string_literal33_tree);
			}

			pushFollow(FOLLOW_join_association_path_expression_in_fetch_join753);
			join_association_path_expression34=join_association_path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, join_association_path_expression34.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:106:1: join_spec : ( ( 'LEFT' ) ( 'OUTER' )? | 'INNER' )? 'JOIN' ;
	public final JPA2Parser.join_spec_return join_spec() throws RecognitionException {
		JPA2Parser.join_spec_return retval = new JPA2Parser.join_spec_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal35=null;
		Token string_literal36=null;
		Token string_literal37=null;
		Token string_literal38=null;

		Object string_literal35_tree=null;
		Object string_literal36_tree=null;
		Object string_literal37_tree=null;
		Object string_literal38_tree=null;

		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:107:6: ( ( ( 'LEFT' ) ( 'OUTER' )? | 'INNER' )? 'JOIN' )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:107:8: ( ( 'LEFT' ) ( 'OUTER' )? | 'INNER' )? 'JOIN'
			{
			root_0 = (Object)adaptor.nil();


			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:107:8: ( ( 'LEFT' ) ( 'OUTER' )? | 'INNER' )?
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
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:107:9: ( 'LEFT' ) ( 'OUTER' )?
					{
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:107:9: ( 'LEFT' )
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:107:10: 'LEFT'
					{
					string_literal35=(Token)match(input,LEFT,FOLLOW_LEFT_in_join_spec767); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal35_tree = (Object)adaptor.create(string_literal35);
					adaptor.addChild(root_0, string_literal35_tree);
					}

					}

					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:107:18: ( 'OUTER' )?
					int alt14=2;
					int LA14_0 = input.LA(1);
					if ( (LA14_0==OUTER) ) {
						alt14=1;
					}
					switch (alt14) {
						case 1 :
							// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:107:19: 'OUTER'
							{
							string_literal36=(Token)match(input,OUTER,FOLLOW_OUTER_in_join_spec771); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							string_literal36_tree = (Object)adaptor.create(string_literal36);
							adaptor.addChild(root_0, string_literal36_tree);
							}

							}
							break;

					}

					}
					break;
				case 2 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:107:31: 'INNER'
					{
					string_literal37=(Token)match(input,INNER,FOLLOW_INNER_in_join_spec777); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal37_tree = (Object)adaptor.create(string_literal37);
					adaptor.addChild(root_0, string_literal37_tree);
					}

					}
					break;

			}

			string_literal38=(Token)match(input,JOIN,FOLLOW_JOIN_in_join_spec782); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal38_tree = (Object)adaptor.create(string_literal38);
			adaptor.addChild(root_0, string_literal38_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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


	public static class join_condition_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "join_condition"
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:108:1: join_condition : 'ON' conditional_expression ;
	public final JPA2Parser.join_condition_return join_condition() throws RecognitionException {
		JPA2Parser.join_condition_return retval = new JPA2Parser.join_condition_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal39=null;
		ParserRuleReturnScope conditional_expression40 =null;

		Object string_literal39_tree=null;

		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:109:6: ( 'ON' conditional_expression )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:109:8: 'ON' conditional_expression
			{
			root_0 = (Object)adaptor.nil();


			string_literal39=(Token)match(input,115,FOLLOW_115_in_join_condition794); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal39_tree = (Object)adaptor.create(string_literal39);
			adaptor.addChild(root_0, string_literal39_tree);
			}

			pushFollow(FOLLOW_conditional_expression_in_join_condition796);
			conditional_expression40=conditional_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_expression40.getTree());

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "join_condition"


	public static class join_association_path_expression_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "join_association_path_expression"
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:112:1: join_association_path_expression : ( identification_variable '.' ( field '.' )* field -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) | 'TREAT(' identification_variable '.' ( field '.' )* ( field )? 'AS' subtype ')' -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) );
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

		Object char_literal42_tree=null;
		Object char_literal44_tree=null;
		Object string_literal46_tree=null;
		Object char_literal48_tree=null;
		Object char_literal50_tree=null;
		Object string_literal52_tree=null;
		Object char_literal54_tree=null;
		RewriteRuleTokenStream stream_125=new RewriteRuleTokenStream(adaptor,"token 125");
		RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
		RewriteRuleTokenStream stream_57=new RewriteRuleTokenStream(adaptor,"token 57");
		RewriteRuleTokenStream stream_75=new RewriteRuleTokenStream(adaptor,"token 75");
		RewriteRuleSubtreeStream stream_identification_variable=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable");
		RewriteRuleSubtreeStream stream_field=new RewriteRuleSubtreeStream(adaptor,"rule field");
		RewriteRuleSubtreeStream stream_subtype=new RewriteRuleSubtreeStream(adaptor,"rule subtype");

		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:113:6: ( identification_variable '.' ( field '.' )* field -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) | 'TREAT(' identification_variable '.' ( field '.' )* ( field )? 'AS' subtype ')' -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) )
			int alt19=2;
			int LA19_0 = input.LA(1);
			if ( (LA19_0==WORD) ) {
				alt19=1;
			}
			else if ( (LA19_0==125) ) {
				alt19=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 19, 0, input);
				throw nvae;
			}

			switch (alt19) {
				case 1 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:113:8: identification_variable '.' ( field '.' )* field
					{
					pushFollow(FOLLOW_identification_variable_in_join_association_path_expression810);
					identification_variable41=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_identification_variable.add(identification_variable41.getTree());
					char_literal42=(Token)match(input,57,FOLLOW_57_in_join_association_path_expression812); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_57.add(char_literal42);

					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:113:36: ( field '.' )*
					loop16:
					while (true) {
						int alt16=2;
						int LA16_0 = input.LA(1);
						if ( (LA16_0==GROUP||LA16_0==WORD) ) {
							int LA16_1 = input.LA(2);
							if ( (LA16_1==57) ) {
								alt16=1;
							}

						}

						switch (alt16) {
						case 1 :
							// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:113:37: field '.'
							{
							pushFollow(FOLLOW_field_in_join_association_path_expression815);
							field43=field();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_field.add(field43.getTree());
							char_literal44=(Token)match(input,57,FOLLOW_57_in_join_association_path_expression816); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_57.add(char_literal44);

							}
							break;

						default :
							break loop16;
						}
					}

					pushFollow(FOLLOW_field_in_join_association_path_expression820);
					field45=field();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_field.add(field45.getTree());
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
						// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:114:13: ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new PathNode(T_SELECTED_FIELD, (identification_variable41!=null?input.toString(identification_variable41.start,identification_variable41.stop):null)), root_1);
						// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:114:73: ( field )*
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
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:115:9: 'TREAT(' identification_variable '.' ( field '.' )* ( field )? 'AS' subtype ')'
					{
					string_literal46=(Token)match(input,125,FOLLOW_125_in_join_association_path_expression854); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_125.add(string_literal46);

					pushFollow(FOLLOW_identification_variable_in_join_association_path_expression856);
					identification_variable47=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_identification_variable.add(identification_variable47.getTree());
					char_literal48=(Token)match(input,57,FOLLOW_57_in_join_association_path_expression858); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_57.add(char_literal48);

					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:115:46: ( field '.' )*
					loop17:
					while (true) {
						int alt17=2;
						int LA17_0 = input.LA(1);
						if ( (LA17_0==GROUP||LA17_0==WORD) ) {
							int LA17_1 = input.LA(2);
							if ( (LA17_1==57) ) {
								alt17=1;
							}

						}

						switch (alt17) {
						case 1 :
							// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:115:47: field '.'
							{
							pushFollow(FOLLOW_field_in_join_association_path_expression861);
							field49=field();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_field.add(field49.getTree());
							char_literal50=(Token)match(input,57,FOLLOW_57_in_join_association_path_expression862); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_57.add(char_literal50);

							}
							break;

						default :
							break loop17;
						}
					}

					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:115:58: ( field )?
					int alt18=2;
					int LA18_0 = input.LA(1);
					if ( (LA18_0==GROUP||LA18_0==WORD) ) {
						alt18=1;
					}
					switch (alt18) {
						case 1 :
							// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:115:58: field
							{
							pushFollow(FOLLOW_field_in_join_association_path_expression866);
							field51=field();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_field.add(field51.getTree());
							}
							break;

					}

					string_literal52=(Token)match(input,75,FOLLOW_75_in_join_association_path_expression869); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_75.add(string_literal52);

					pushFollow(FOLLOW_subtype_in_join_association_path_expression871);
					subtype53=subtype();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_subtype.add(subtype53.getTree());
					char_literal54=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_join_association_path_expression873); if (state.failed) return retval; 
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
					// 116:10: -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
					{
						// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:116:13: ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new PathNode(T_SELECTED_FIELD, (identification_variable47!=null?input.toString(identification_variable47.start,identification_variable47.stop):null)), root_1);
						// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:116:73: ( field )*
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

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:119:1: collection_member_declaration : 'IN' '(' path_expression ')' ( 'AS' )? identification_variable -> ^( T_COLLECTION_MEMBER[$identification_variable.text] path_expression ) ;
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
		RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
		RewriteRuleTokenStream stream_95=new RewriteRuleTokenStream(adaptor,"token 95");
		RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
		RewriteRuleTokenStream stream_75=new RewriteRuleTokenStream(adaptor,"token 75");
		RewriteRuleSubtreeStream stream_identification_variable=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable");
		RewriteRuleSubtreeStream stream_path_expression=new RewriteRuleSubtreeStream(adaptor,"rule path_expression");

		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:120:5: ( 'IN' '(' path_expression ')' ( 'AS' )? identification_variable -> ^( T_COLLECTION_MEMBER[$identification_variable.text] path_expression ) )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:120:7: 'IN' '(' path_expression ')' ( 'AS' )? identification_variable
			{
			string_literal55=(Token)match(input,95,FOLLOW_95_in_collection_member_declaration910); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_95.add(string_literal55);

			char_literal56=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_collection_member_declaration911); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_LPAREN.add(char_literal56);

			pushFollow(FOLLOW_path_expression_in_collection_member_declaration913);
			path_expression57=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_path_expression.add(path_expression57.getTree());
			char_literal58=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_collection_member_declaration915); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_RPAREN.add(char_literal58);

			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:120:35: ( 'AS' )?
			int alt20=2;
			int LA20_0 = input.LA(1);
			if ( (LA20_0==75) ) {
				alt20=1;
			}
			switch (alt20) {
				case 1 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:120:36: 'AS'
					{
					string_literal59=(Token)match(input,75,FOLLOW_75_in_collection_member_declaration918); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_75.add(string_literal59);

					}
					break;

			}

			pushFollow(FOLLOW_identification_variable_in_collection_member_declaration922);
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
			// 121:5: -> ^( T_COLLECTION_MEMBER[$identification_variable.text] path_expression )
			{
				// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:121:8: ^( T_COLLECTION_MEMBER[$identification_variable.text] path_expression )
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:123:1: qualified_identification_variable : ( map_field_identification_variable | 'ENTRY(' identification_variable ')' );
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
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:124:5: ( map_field_identification_variable | 'ENTRY(' identification_variable ')' )
			int alt21=2;
			int LA21_0 = input.LA(1);
			if ( (LA21_0==98||LA21_0==130) ) {
				alt21=1;
			}
			else if ( (LA21_0==89) ) {
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
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:124:7: map_field_identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_map_field_identification_variable_in_qualified_identification_variable951);
					map_field_identification_variable61=map_field_identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, map_field_identification_variable61.getTree());

					}
					break;
				case 2 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:125:7: 'ENTRY(' identification_variable ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal62=(Token)match(input,89,FOLLOW_89_in_qualified_identification_variable959); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal62_tree = (Object)adaptor.create(string_literal62);
					adaptor.addChild(root_0, string_literal62_tree);
					}

					pushFollow(FOLLOW_identification_variable_in_qualified_identification_variable960);
					identification_variable63=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable63.getTree());

					char_literal64=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_qualified_identification_variable961); if (state.failed) return retval;
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:126:1: map_field_identification_variable : ( 'KEY(' identification_variable ')' | 'VALUE(' identification_variable ')' );
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
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:126:35: ( 'KEY(' identification_variable ')' | 'VALUE(' identification_variable ')' )
			int alt22=2;
			int LA22_0 = input.LA(1);
			if ( (LA22_0==98) ) {
				alt22=1;
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
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:126:37: 'KEY(' identification_variable ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal65=(Token)match(input,98,FOLLOW_98_in_map_field_identification_variable968); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal65_tree = (Object)adaptor.create(string_literal65);
					adaptor.addChild(root_0, string_literal65_tree);
					}

					pushFollow(FOLLOW_identification_variable_in_map_field_identification_variable969);
					identification_variable66=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable66.getTree());

					char_literal67=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_map_field_identification_variable970); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal67_tree = (Object)adaptor.create(char_literal67);
					adaptor.addChild(root_0, char_literal67_tree);
					}

					}
					break;
				case 2 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:126:72: 'VALUE(' identification_variable ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal68=(Token)match(input,130,FOLLOW_130_in_map_field_identification_variable974); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal68_tree = (Object)adaptor.create(string_literal68);
					adaptor.addChild(root_0, string_literal68_tree);
					}

					pushFollow(FOLLOW_identification_variable_in_map_field_identification_variable975);
					identification_variable69=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable69.getTree());

					char_literal70=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_map_field_identification_variable976); if (state.failed) return retval;
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:129:1: path_expression : identification_variable '.' ( field '.' )* field -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) ;
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
		RewriteRuleTokenStream stream_57=new RewriteRuleTokenStream(adaptor,"token 57");
		RewriteRuleSubtreeStream stream_identification_variable=new RewriteRuleSubtreeStream(adaptor,"rule identification_variable");
		RewriteRuleSubtreeStream stream_field=new RewriteRuleSubtreeStream(adaptor,"rule field");

		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:130:5: ( identification_variable '.' ( field '.' )* field -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* ) )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:130:8: identification_variable '.' ( field '.' )* field
			{
			pushFollow(FOLLOW_identification_variable_in_path_expression990);
			identification_variable71=identification_variable();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_identification_variable.add(identification_variable71.getTree());
			char_literal72=(Token)match(input,57,FOLLOW_57_in_path_expression992); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_57.add(char_literal72);

			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:130:36: ( field '.' )*
			loop23:
			while (true) {
				int alt23=2;
				int LA23_0 = input.LA(1);
				if ( (LA23_0==GROUP||LA23_0==WORD) ) {
					int LA23_1 = input.LA(2);
					if ( (LA23_1==57) ) {
						alt23=1;
					}

				}

				switch (alt23) {
				case 1 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:130:37: field '.'
					{
					pushFollow(FOLLOW_field_in_path_expression995);
					field73=field();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_field.add(field73.getTree());
					char_literal74=(Token)match(input,57,FOLLOW_57_in_path_expression996); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_57.add(char_literal74);

					}
					break;

				default :
					break loop23;
				}
			}

			pushFollow(FOLLOW_field_in_path_expression1000);
			field75=field();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_field.add(field75.getTree());
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
			// 131:5: -> ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
			{
				// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:131:8: ^( T_SELECTED_FIELD[$identification_variable.text] ( field )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new PathNode(T_SELECTED_FIELD, (identification_variable71!=null?input.toString(identification_variable71.start,identification_variable71.stop):null)), root_1);
				// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:131:68: ( field )*
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:136:1: general_identification_variable : ( identification_variable | map_field_identification_variable );
	public final JPA2Parser.general_identification_variable_return general_identification_variable() throws RecognitionException {
		JPA2Parser.general_identification_variable_return retval = new JPA2Parser.general_identification_variable_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope identification_variable76 =null;
		ParserRuleReturnScope map_field_identification_variable77 =null;


		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:137:5: ( identification_variable | map_field_identification_variable )
			int alt24=2;
			int LA24_0 = input.LA(1);
			if ( (LA24_0==WORD) ) {
				alt24=1;
			}
			else if ( (LA24_0==98||LA24_0==130) ) {
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
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:137:7: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_general_identification_variable1038);
					identification_variable76=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable76.getTree());

					}
					break;
				case 2 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:138:7: map_field_identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_map_field_identification_variable_in_general_identification_variable1046);
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:139:1: update_clause : entity_name ( ( 'AS' )? identification_variable )? 'SET' update_item ( ',' update_item )* ;
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
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:140:5: ( entity_name ( ( 'AS' )? identification_variable )? 'SET' update_item ( ',' update_item )* )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:140:7: entity_name ( ( 'AS' )? identification_variable )? 'SET' update_item ( ',' update_item )*
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_entity_name_in_update_clause1057);
			entity_name78=entity_name();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_name78.getTree());

			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:140:19: ( ( 'AS' )? identification_variable )?
			int alt26=2;
			int LA26_0 = input.LA(1);
			if ( (LA26_0==WORD||LA26_0==75) ) {
				alt26=1;
			}
			switch (alt26) {
				case 1 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:140:20: ( 'AS' )? identification_variable
					{
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:140:20: ( 'AS' )?
					int alt25=2;
					int LA25_0 = input.LA(1);
					if ( (LA25_0==75) ) {
						alt25=1;
					}
					switch (alt25) {
						case 1 :
							// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:140:21: 'AS'
							{
							string_literal79=(Token)match(input,75,FOLLOW_75_in_update_clause1061); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							string_literal79_tree = (Object)adaptor.create(string_literal79);
							adaptor.addChild(root_0, string_literal79_tree);
							}

							}
							break;

					}

					pushFollow(FOLLOW_identification_variable_in_update_clause1065);
					identification_variable80=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable80.getTree());

					}
					break;

			}

			string_literal81=(Token)match(input,118,FOLLOW_118_in_update_clause1069); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal81_tree = (Object)adaptor.create(string_literal81);
			adaptor.addChild(root_0, string_literal81_tree);
			}

			pushFollow(FOLLOW_update_item_in_update_clause1071);
			update_item82=update_item();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, update_item82.getTree());

			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:140:72: ( ',' update_item )*
			loop27:
			while (true) {
				int alt27=2;
				int LA27_0 = input.LA(1);
				if ( (LA27_0==55) ) {
					alt27=1;
				}

				switch (alt27) {
				case 1 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:140:73: ',' update_item
					{
					char_literal83=(Token)match(input,55,FOLLOW_55_in_update_clause1074); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal83_tree = (Object)adaptor.create(char_literal83);
					adaptor.addChild(root_0, char_literal83_tree);
					}

					pushFollow(FOLLOW_update_item_in_update_clause1076);
					update_item84=update_item();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, update_item84.getTree());

					}
					break;

				default :
					break loop27;
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:141:1: update_item : ( identification_variable '.' ) ( single_valued_embeddable_object_field '.' )* single_valued_object_field '=' new_value ;
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
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:142:5: ( ( identification_variable '.' ) ( single_valued_embeddable_object_field '.' )* single_valued_object_field '=' new_value )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:142:7: ( identification_variable '.' ) ( single_valued_embeddable_object_field '.' )* single_valued_object_field '=' new_value
			{
			root_0 = (Object)adaptor.nil();


			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:142:7: ( identification_variable '.' )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:142:8: identification_variable '.'
			{
			pushFollow(FOLLOW_identification_variable_in_update_item1090);
			identification_variable85=identification_variable();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable85.getTree());

			char_literal86=(Token)match(input,57,FOLLOW_57_in_update_item1091); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal86_tree = (Object)adaptor.create(char_literal86);
			adaptor.addChild(root_0, char_literal86_tree);
			}

			}

			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:142:35: ( single_valued_embeddable_object_field '.' )*
			loop28:
			while (true) {
				int alt28=2;
				int LA28_0 = input.LA(1);
				if ( (LA28_0==WORD) ) {
					int LA28_1 = input.LA(2);
					if ( (LA28_1==57) ) {
						alt28=1;
					}

				}

				switch (alt28) {
				case 1 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:142:36: single_valued_embeddable_object_field '.'
					{
					pushFollow(FOLLOW_single_valued_embeddable_object_field_in_update_item1094);
					single_valued_embeddable_object_field87=single_valued_embeddable_object_field();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, single_valued_embeddable_object_field87.getTree());

					char_literal88=(Token)match(input,57,FOLLOW_57_in_update_item1095); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal88_tree = (Object)adaptor.create(char_literal88);
					adaptor.addChild(root_0, char_literal88_tree);
					}

					}
					break;

				default :
					break loop28;
				}
			}

			pushFollow(FOLLOW_single_valued_object_field_in_update_item1098);
			single_valued_object_field89=single_valued_object_field();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, single_valued_object_field89.getTree());

			char_literal90=(Token)match(input,63,FOLLOW_63_in_update_item1100); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal90_tree = (Object)adaptor.create(char_literal90);
			adaptor.addChild(root_0, char_literal90_tree);
			}

			pushFollow(FOLLOW_new_value_in_update_item1102);
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:143:1: new_value : ( scalar_expression | simple_entity_expression | 'NULL' );
	public final JPA2Parser.new_value_return new_value() throws RecognitionException {
		JPA2Parser.new_value_return retval = new JPA2Parser.new_value_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal94=null;
		ParserRuleReturnScope scalar_expression92 =null;
		ParserRuleReturnScope simple_entity_expression93 =null;

		Object string_literal94_tree=null;

		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:144:5: ( scalar_expression | simple_entity_expression | 'NULL' )
			int alt29=3;
			switch ( input.LA(1) ) {
			case AVG:
			case COUNT:
			case INT_NUMERAL:
			case LPAREN:
			case MAX:
			case MIN:
			case STRING_LITERAL:
			case SUM:
			case 54:
			case 56:
			case 59:
			case 72:
			case 78:
			case 79:
			case 80:
			case 81:
			case 82:
			case 83:
			case 93:
			case 96:
			case 100:
			case 102:
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
				alt29=1;
				}
				break;
			case WORD:
				{
				int LA29_2 = input.LA(2);
				if ( (synpred30_JPA2()) ) {
					alt29=1;
				}
				else if ( (synpred31_JPA2()) ) {
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
			case 66:
				{
				int LA29_3 = input.LA(2);
				if ( (LA29_3==59) ) {
					int LA29_8 = input.LA(3);
					if ( (LA29_8==INT_NUMERAL) ) {
						int LA29_9 = input.LA(4);
						if ( (synpred30_JPA2()) ) {
							alt29=1;
						}
						else if ( (synpred31_JPA2()) ) {
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
					if ( (synpred30_JPA2()) ) {
						alt29=1;
					}
					else if ( (synpred31_JPA2()) ) {
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
				if ( (synpred30_JPA2()) ) {
					alt29=1;
				}
				else if ( (synpred31_JPA2()) ) {
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
			case 52:
				{
				int LA29_5 = input.LA(2);
				if ( (LA29_5==WORD) ) {
					int LA29_10 = input.LA(3);
					if ( (LA29_10==136) ) {
						int LA29_11 = input.LA(4);
						if ( (synpred30_JPA2()) ) {
							alt29=1;
						}
						else if ( (synpred31_JPA2()) ) {
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
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:144:7: scalar_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_scalar_expression_in_new_value1113);
					scalar_expression92=scalar_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression92.getTree());

					}
					break;
				case 2 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:145:7: simple_entity_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_simple_entity_expression_in_new_value1121);
					simple_entity_expression93=simple_entity_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_entity_expression93.getTree());

					}
					break;
				case 3 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:146:7: 'NULL'
					{
					root_0 = (Object)adaptor.nil();


					string_literal94=(Token)match(input,111,FOLLOW_111_in_new_value1129); if (state.failed) return retval;
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:147:1: delete_clause : entity_name ( ( 'AS' )? identification_variable )? ;
	public final JPA2Parser.delete_clause_return delete_clause() throws RecognitionException {
		JPA2Parser.delete_clause_return retval = new JPA2Parser.delete_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal96=null;
		ParserRuleReturnScope entity_name95 =null;
		ParserRuleReturnScope identification_variable97 =null;

		Object string_literal96_tree=null;

		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:148:5: ( entity_name ( ( 'AS' )? identification_variable )? )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:148:7: entity_name ( ( 'AS' )? identification_variable )?
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_entity_name_in_delete_clause1140);
			entity_name95=entity_name();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_name95.getTree());

			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:148:19: ( ( 'AS' )? identification_variable )?
			int alt31=2;
			int LA31_0 = input.LA(1);
			if ( (LA31_0==WORD||LA31_0==75) ) {
				alt31=1;
			}
			switch (alt31) {
				case 1 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:148:20: ( 'AS' )? identification_variable
					{
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:148:20: ( 'AS' )?
					int alt30=2;
					int LA30_0 = input.LA(1);
					if ( (LA30_0==75) ) {
						alt30=1;
					}
					switch (alt30) {
						case 1 :
							// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:148:21: 'AS'
							{
							string_literal96=(Token)match(input,75,FOLLOW_75_in_delete_clause1144); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							string_literal96_tree = (Object)adaptor.create(string_literal96);
							adaptor.addChild(root_0, string_literal96_tree);
							}

							}
							break;

					}

					pushFollow(FOLLOW_identification_variable_in_delete_clause1148);
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:149:1: select_clause : ( 'DISTINCT' )? select_item ( ',' select_item )* -> ^( T_SELECTED_ITEMS ( 'DISTINCT' )? ( ^( T_SELECTED_ITEM[] select_item ) )* ) ;
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
		RewriteRuleTokenStream stream_55=new RewriteRuleTokenStream(adaptor,"token 55");
		RewriteRuleTokenStream stream_DISTINCT=new RewriteRuleTokenStream(adaptor,"token DISTINCT");
		RewriteRuleSubtreeStream stream_select_item=new RewriteRuleSubtreeStream(adaptor,"rule select_item");

		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:150:5: ( ( 'DISTINCT' )? select_item ( ',' select_item )* -> ^( T_SELECTED_ITEMS ( 'DISTINCT' )? ( ^( T_SELECTED_ITEM[] select_item ) )* ) )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:150:7: ( 'DISTINCT' )? select_item ( ',' select_item )*
			{
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:150:7: ( 'DISTINCT' )?
			int alt32=2;
			int LA32_0 = input.LA(1);
			if ( (LA32_0==DISTINCT) ) {
				alt32=1;
			}
			switch (alt32) {
				case 1 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:150:8: 'DISTINCT'
					{
					string_literal98=(Token)match(input,DISTINCT,FOLLOW_DISTINCT_in_select_clause1162); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_DISTINCT.add(string_literal98);

					}
					break;

			}

			pushFollow(FOLLOW_select_item_in_select_clause1166);
			select_item99=select_item();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_select_item.add(select_item99.getTree());
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:150:33: ( ',' select_item )*
			loop33:
			while (true) {
				int alt33=2;
				int LA33_0 = input.LA(1);
				if ( (LA33_0==55) ) {
					alt33=1;
				}

				switch (alt33) {
				case 1 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:150:34: ',' select_item
					{
					char_literal100=(Token)match(input,55,FOLLOW_55_in_select_clause1169); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_55.add(char_literal100);

					pushFollow(FOLLOW_select_item_in_select_clause1171);
					select_item101=select_item();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_select_item.add(select_item101.getTree());
					}
					break;

				default :
					break loop33;
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
			// 151:5: -> ^( T_SELECTED_ITEMS ( 'DISTINCT' )? ( ^( T_SELECTED_ITEM[] select_item ) )* )
			{
				// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:151:8: ^( T_SELECTED_ITEMS ( 'DISTINCT' )? ( ^( T_SELECTED_ITEM[] select_item ) )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(T_SELECTED_ITEMS, "T_SELECTED_ITEMS"), root_1);
				// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:151:27: ( 'DISTINCT' )?
				if ( stream_DISTINCT.hasNext() ) {
					adaptor.addChild(root_1, stream_DISTINCT.nextNode());
				}
				stream_DISTINCT.reset();

				// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:151:41: ( ^( T_SELECTED_ITEM[] select_item ) )*
				while ( stream_select_item.hasNext() ) {
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:151:41: ^( T_SELECTED_ITEM[] select_item )
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:152:1: select_item : select_expression ( ( 'AS' )? result_variable )? ;
	public final JPA2Parser.select_item_return select_item() throws RecognitionException {
		JPA2Parser.select_item_return retval = new JPA2Parser.select_item_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal103=null;
		ParserRuleReturnScope select_expression102 =null;
		ParserRuleReturnScope result_variable104 =null;

		Object string_literal103_tree=null;

		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:153:5: ( select_expression ( ( 'AS' )? result_variable )? )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:153:7: select_expression ( ( 'AS' )? result_variable )?
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_select_expression_in_select_item1210);
			select_expression102=select_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, select_expression102.getTree());

			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:153:25: ( ( 'AS' )? result_variable )?
			int alt35=2;
			int LA35_0 = input.LA(1);
			if ( (LA35_0==WORD||LA35_0==75) ) {
				alt35=1;
			}
			switch (alt35) {
				case 1 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:153:26: ( 'AS' )? result_variable
					{
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:153:26: ( 'AS' )?
					int alt34=2;
					int LA34_0 = input.LA(1);
					if ( (LA34_0==75) ) {
						alt34=1;
					}
					switch (alt34) {
						case 1 :
							// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:153:27: 'AS'
							{
							string_literal103=(Token)match(input,75,FOLLOW_75_in_select_item1214); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							string_literal103_tree = (Object)adaptor.create(string_literal103);
							adaptor.addChild(root_0, string_literal103_tree);
							}

							}
							break;

					}

					pushFollow(FOLLOW_result_variable_in_select_item1218);
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:154:1: select_expression : ( path_expression | identification_variable -> ^( T_SELECTED_ENTITY[$identification_variable.text] ) | scalar_expression | aggregate_expression | 'OBJECT' '(' identification_variable ')' | constructor_expression );
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
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:155:5: ( path_expression | identification_variable -> ^( T_SELECTED_ENTITY[$identification_variable.text] ) | scalar_expression | aggregate_expression | 'OBJECT' '(' identification_variable ')' | constructor_expression )
			int alt36=6;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA36_1 = input.LA(2);
				if ( (synpred38_JPA2()) ) {
					alt36=1;
				}
				else if ( (synpred39_JPA2()) ) {
					alt36=2;
				}
				else if ( (synpred40_JPA2()) ) {
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
			case LPAREN:
			case NAMED_PARAMETER:
			case STRING_LITERAL:
			case 52:
			case 54:
			case 56:
			case 59:
			case 66:
			case 72:
			case 78:
			case 79:
			case 80:
			case 81:
			case 82:
			case 83:
			case 96:
			case 100:
			case 102:
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
				alt36=3;
				}
				break;
			case COUNT:
				{
				int LA36_16 = input.LA(2);
				if ( (synpred40_JPA2()) ) {
					alt36=3;
				}
				else if ( (synpred41_JPA2()) ) {
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
				if ( (synpred40_JPA2()) ) {
					alt36=3;
				}
				else if ( (synpred41_JPA2()) ) {
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
			case 93:
				{
				int LA36_18 = input.LA(2);
				if ( (synpred40_JPA2()) ) {
					alt36=3;
				}
				else if ( (synpred41_JPA2()) ) {
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
			case 113:
				{
				alt36=5;
				}
				break;
			case 108:
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
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:155:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_select_expression1231);
					path_expression105=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression105.getTree());

					}
					break;
				case 2 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:156:7: identification_variable
					{
					pushFollow(FOLLOW_identification_variable_in_select_expression1239);
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
					// 156:31: -> ^( T_SELECTED_ENTITY[$identification_variable.text] )
					{
						// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:156:34: ^( T_SELECTED_ENTITY[$identification_variable.text] )
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
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:157:7: scalar_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_scalar_expression_in_select_expression1257);
					scalar_expression107=scalar_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression107.getTree());

					}
					break;
				case 4 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:158:7: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_select_expression1265);
					aggregate_expression108=aggregate_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression108.getTree());

					}
					break;
				case 5 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:159:7: 'OBJECT' '(' identification_variable ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal109=(Token)match(input,113,FOLLOW_113_in_select_expression1273); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal109_tree = (Object)adaptor.create(string_literal109);
					adaptor.addChild(root_0, string_literal109_tree);
					}

					char_literal110=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_select_expression1275); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal110_tree = (Object)adaptor.create(char_literal110);
					adaptor.addChild(root_0, char_literal110_tree);
					}

					pushFollow(FOLLOW_identification_variable_in_select_expression1276);
					identification_variable111=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable111.getTree());

					char_literal112=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_select_expression1277); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal112_tree = (Object)adaptor.create(char_literal112);
					adaptor.addChild(root_0, char_literal112_tree);
					}

					}
					break;
				case 6 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:160:7: constructor_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_constructor_expression_in_select_expression1285);
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:161:1: constructor_expression : 'NEW' constructor_name '(' constructor_item ( ',' constructor_item )* ')' ;
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
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:162:5: ( 'NEW' constructor_name '(' constructor_item ( ',' constructor_item )* ')' )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:162:7: 'NEW' constructor_name '(' constructor_item ( ',' constructor_item )* ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal114=(Token)match(input,108,FOLLOW_108_in_constructor_expression1296); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal114_tree = (Object)adaptor.create(string_literal114);
			adaptor.addChild(root_0, string_literal114_tree);
			}

			pushFollow(FOLLOW_constructor_name_in_constructor_expression1298);
			constructor_name115=constructor_name();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, constructor_name115.getTree());

			char_literal116=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_constructor_expression1300); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal116_tree = (Object)adaptor.create(char_literal116);
			adaptor.addChild(root_0, char_literal116_tree);
			}

			pushFollow(FOLLOW_constructor_item_in_constructor_expression1302);
			constructor_item117=constructor_item();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, constructor_item117.getTree());

			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:162:51: ( ',' constructor_item )*
			loop37:
			while (true) {
				int alt37=2;
				int LA37_0 = input.LA(1);
				if ( (LA37_0==55) ) {
					alt37=1;
				}

				switch (alt37) {
				case 1 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:162:52: ',' constructor_item
					{
					char_literal118=(Token)match(input,55,FOLLOW_55_in_constructor_expression1305); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal118_tree = (Object)adaptor.create(char_literal118);
					adaptor.addChild(root_0, char_literal118_tree);
					}

					pushFollow(FOLLOW_constructor_item_in_constructor_expression1307);
					constructor_item119=constructor_item();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, constructor_item119.getTree());

					}
					break;

				default :
					break loop37;
				}
			}

			char_literal120=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_constructor_expression1311); if (state.failed) return retval;
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:163:1: constructor_item : ( path_expression | scalar_expression | aggregate_expression | identification_variable );
	public final JPA2Parser.constructor_item_return constructor_item() throws RecognitionException {
		JPA2Parser.constructor_item_return retval = new JPA2Parser.constructor_item_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression121 =null;
		ParserRuleReturnScope scalar_expression122 =null;
		ParserRuleReturnScope aggregate_expression123 =null;
		ParserRuleReturnScope identification_variable124 =null;


		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:164:5: ( path_expression | scalar_expression | aggregate_expression | identification_variable )
			int alt38=4;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA38_1 = input.LA(2);
				if ( (synpred44_JPA2()) ) {
					alt38=1;
				}
				else if ( (synpred45_JPA2()) ) {
					alt38=2;
				}
				else if ( (true) ) {
					alt38=4;
				}

				}
				break;
			case INT_NUMERAL:
			case LPAREN:
			case NAMED_PARAMETER:
			case STRING_LITERAL:
			case 52:
			case 54:
			case 56:
			case 59:
			case 66:
			case 72:
			case 78:
			case 79:
			case 80:
			case 81:
			case 82:
			case 83:
			case 96:
			case 100:
			case 102:
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
				alt38=2;
				}
				break;
			case COUNT:
				{
				int LA38_16 = input.LA(2);
				if ( (synpred45_JPA2()) ) {
					alt38=2;
				}
				else if ( (synpred46_JPA2()) ) {
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
				if ( (synpred45_JPA2()) ) {
					alt38=2;
				}
				else if ( (synpred46_JPA2()) ) {
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
			case 93:
				{
				int LA38_18 = input.LA(2);
				if ( (synpred45_JPA2()) ) {
					alt38=2;
				}
				else if ( (synpred46_JPA2()) ) {
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
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 38, 0, input);
				throw nvae;
			}
			switch (alt38) {
				case 1 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:164:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_constructor_item1322);
					path_expression121=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression121.getTree());

					}
					break;
				case 2 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:165:7: scalar_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_scalar_expression_in_constructor_item1330);
					scalar_expression122=scalar_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression122.getTree());

					}
					break;
				case 3 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:166:7: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_constructor_item1338);
					aggregate_expression123=aggregate_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression123.getTree());

					}
					break;
				case 4 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:167:7: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_constructor_item1346);
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:168:1: aggregate_expression : ( aggregate_expression_function_name '(' ( DISTINCT )? path_expression ')' -> ^( T_AGGREGATE_EXPR[] aggregate_expression_function_name '(' ( 'DISTINCT' )? path_expression ')' ) | 'COUNT' '(' ( DISTINCT )? count_argument ')' -> ^( T_AGGREGATE_EXPR[] 'COUNT' '(' ( 'DISTINCT' )? count_argument ')' ) | function_invocation );
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
		RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
		RewriteRuleTokenStream stream_COUNT=new RewriteRuleTokenStream(adaptor,"token COUNT");
		RewriteRuleTokenStream stream_DISTINCT=new RewriteRuleTokenStream(adaptor,"token DISTINCT");
		RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
		RewriteRuleSubtreeStream stream_path_expression=new RewriteRuleSubtreeStream(adaptor,"rule path_expression");
		RewriteRuleSubtreeStream stream_aggregate_expression_function_name=new RewriteRuleSubtreeStream(adaptor,"rule aggregate_expression_function_name");
		RewriteRuleSubtreeStream stream_count_argument=new RewriteRuleSubtreeStream(adaptor,"rule count_argument");

		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:169:5: ( aggregate_expression_function_name '(' ( DISTINCT )? path_expression ')' -> ^( T_AGGREGATE_EXPR[] aggregate_expression_function_name '(' ( 'DISTINCT' )? path_expression ')' ) | 'COUNT' '(' ( DISTINCT )? count_argument ')' -> ^( T_AGGREGATE_EXPR[] 'COUNT' '(' ( 'DISTINCT' )? count_argument ')' ) | function_invocation )
			int alt41=3;
			alt41 = dfa41.predict(input);
			switch (alt41) {
				case 1 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:169:7: aggregate_expression_function_name '(' ( DISTINCT )? path_expression ')'
					{
					pushFollow(FOLLOW_aggregate_expression_function_name_in_aggregate_expression1357);
					aggregate_expression_function_name125=aggregate_expression_function_name();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_aggregate_expression_function_name.add(aggregate_expression_function_name125.getTree());
					char_literal126=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_aggregate_expression1359); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_LPAREN.add(char_literal126);

					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:169:45: ( DISTINCT )?
					int alt39=2;
					int LA39_0 = input.LA(1);
					if ( (LA39_0==DISTINCT) ) {
						alt39=1;
					}
					switch (alt39) {
						case 1 :
							// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:169:46: DISTINCT
							{
							DISTINCT127=(Token)match(input,DISTINCT,FOLLOW_DISTINCT_in_aggregate_expression1361); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_DISTINCT.add(DISTINCT127);

							}
							break;

					}

					pushFollow(FOLLOW_path_expression_in_aggregate_expression1365);
					path_expression128=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_path_expression.add(path_expression128.getTree());
					char_literal129=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_aggregate_expression1366); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_RPAREN.add(char_literal129);

					// AST REWRITE
					// elements: DISTINCT, aggregate_expression_function_name, RPAREN, path_expression, LPAREN
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 170:5: -> ^( T_AGGREGATE_EXPR[] aggregate_expression_function_name '(' ( 'DISTINCT' )? path_expression ')' )
					{
						// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:170:8: ^( T_AGGREGATE_EXPR[] aggregate_expression_function_name '(' ( 'DISTINCT' )? path_expression ')' )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new AggregateExpressionNode(T_AGGREGATE_EXPR), root_1);
						adaptor.addChild(root_1, stream_aggregate_expression_function_name.nextTree());
						adaptor.addChild(root_1, stream_LPAREN.nextNode());
						// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:170:93: ( 'DISTINCT' )?
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
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:171:7: 'COUNT' '(' ( DISTINCT )? count_argument ')'
					{
					string_literal130=(Token)match(input,COUNT,FOLLOW_COUNT_in_aggregate_expression1400); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_COUNT.add(string_literal130);

					char_literal131=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_aggregate_expression1402); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_LPAREN.add(char_literal131);

					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:171:18: ( DISTINCT )?
					int alt40=2;
					int LA40_0 = input.LA(1);
					if ( (LA40_0==DISTINCT) ) {
						alt40=1;
					}
					switch (alt40) {
						case 1 :
							// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:171:19: DISTINCT
							{
							DISTINCT132=(Token)match(input,DISTINCT,FOLLOW_DISTINCT_in_aggregate_expression1404); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_DISTINCT.add(DISTINCT132);

							}
							break;

					}

					pushFollow(FOLLOW_count_argument_in_aggregate_expression1408);
					count_argument133=count_argument();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_count_argument.add(count_argument133.getTree());
					char_literal134=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_aggregate_expression1410); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_RPAREN.add(char_literal134);

					// AST REWRITE
					// elements: RPAREN, LPAREN, count_argument, DISTINCT, COUNT
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 172:5: -> ^( T_AGGREGATE_EXPR[] 'COUNT' '(' ( 'DISTINCT' )? count_argument ')' )
					{
						// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:172:8: ^( T_AGGREGATE_EXPR[] 'COUNT' '(' ( 'DISTINCT' )? count_argument ')' )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new AggregateExpressionNode(T_AGGREGATE_EXPR), root_1);
						adaptor.addChild(root_1, stream_COUNT.nextNode());
						adaptor.addChild(root_1, stream_LPAREN.nextNode());
						// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:172:66: ( 'DISTINCT' )?
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
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:173:7: function_invocation
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_function_invocation_in_aggregate_expression1445);
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:174:1: aggregate_expression_function_name : ( 'AVG' | 'MAX' | 'MIN' | 'SUM' | 'COUNT' );
	public final JPA2Parser.aggregate_expression_function_name_return aggregate_expression_function_name() throws RecognitionException {
		JPA2Parser.aggregate_expression_function_name_return retval = new JPA2Parser.aggregate_expression_function_name_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set136=null;

		Object set136_tree=null;

		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:175:5: ( 'AVG' | 'MAX' | 'MIN' | 'SUM' | 'COUNT' )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:176:1: count_argument : ( identification_variable | path_expression );
	public final JPA2Parser.count_argument_return count_argument() throws RecognitionException {
		JPA2Parser.count_argument_return retval = new JPA2Parser.count_argument_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope identification_variable137 =null;
		ParserRuleReturnScope path_expression138 =null;


		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:177:5: ( identification_variable | path_expression )
			int alt42=2;
			int LA42_0 = input.LA(1);
			if ( (LA42_0==WORD) ) {
				int LA42_1 = input.LA(2);
				if ( (LA42_1==RPAREN) ) {
					alt42=1;
				}
				else if ( (LA42_1==57) ) {
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
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:177:7: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_count_argument1482);
					identification_variable137=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable137.getTree());

					}
					break;
				case 2 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:177:33: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_count_argument1486);
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:178:1: where_clause : wh= 'WHERE' conditional_expression -> ^( T_CONDITION[$wh] conditional_expression ) ;
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
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:179:5: (wh= 'WHERE' conditional_expression -> ^( T_CONDITION[$wh] conditional_expression ) )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:179:7: wh= 'WHERE' conditional_expression
			{
			wh=(Token)match(input,132,FOLLOW_132_in_where_clause1499); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_132.add(wh);

			pushFollow(FOLLOW_conditional_expression_in_where_clause1501);
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
			// 179:40: -> ^( T_CONDITION[$wh] conditional_expression )
			{
				// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:179:43: ^( T_CONDITION[$wh] conditional_expression )
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:180:1: groupby_clause : 'GROUP' 'BY' groupby_item ( ',' groupby_item )* -> ^( T_GROUP_BY[] 'GROUP' 'BY' ( groupby_item )* ) ;
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
		RewriteRuleTokenStream stream_GROUP=new RewriteRuleTokenStream(adaptor,"token GROUP");
		RewriteRuleTokenStream stream_BY=new RewriteRuleTokenStream(adaptor,"token BY");
		RewriteRuleTokenStream stream_55=new RewriteRuleTokenStream(adaptor,"token 55");
		RewriteRuleSubtreeStream stream_groupby_item=new RewriteRuleSubtreeStream(adaptor,"rule groupby_item");

		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:181:5: ( 'GROUP' 'BY' groupby_item ( ',' groupby_item )* -> ^( T_GROUP_BY[] 'GROUP' 'BY' ( groupby_item )* ) )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:181:7: 'GROUP' 'BY' groupby_item ( ',' groupby_item )*
			{
			string_literal140=(Token)match(input,GROUP,FOLLOW_GROUP_in_groupby_clause1523); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_GROUP.add(string_literal140);

			string_literal141=(Token)match(input,BY,FOLLOW_BY_in_groupby_clause1525); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_BY.add(string_literal141);

			pushFollow(FOLLOW_groupby_item_in_groupby_clause1527);
			groupby_item142=groupby_item();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_groupby_item.add(groupby_item142.getTree());
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:181:33: ( ',' groupby_item )*
			loop43:
			while (true) {
				int alt43=2;
				int LA43_0 = input.LA(1);
				if ( (LA43_0==55) ) {
					alt43=1;
				}

				switch (alt43) {
				case 1 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:181:34: ',' groupby_item
					{
					char_literal143=(Token)match(input,55,FOLLOW_55_in_groupby_clause1530); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_55.add(char_literal143);

					pushFollow(FOLLOW_groupby_item_in_groupby_clause1532);
					groupby_item144=groupby_item();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_groupby_item.add(groupby_item144.getTree());
					}
					break;

				default :
					break loop43;
				}
			}

			// AST REWRITE
			// elements: groupby_item, GROUP, BY
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 182:5: -> ^( T_GROUP_BY[] 'GROUP' 'BY' ( groupby_item )* )
			{
				// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:182:8: ^( T_GROUP_BY[] 'GROUP' 'BY' ( groupby_item )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new GroupByNode(T_GROUP_BY), root_1);
				adaptor.addChild(root_1, stream_GROUP.nextNode());
				adaptor.addChild(root_1, stream_BY.nextNode());
				// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:182:49: ( groupby_item )*
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:183:1: groupby_item : ( path_expression | identification_variable );
	public final JPA2Parser.groupby_item_return groupby_item() throws RecognitionException {
		JPA2Parser.groupby_item_return retval = new JPA2Parser.groupby_item_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression145 =null;
		ParserRuleReturnScope identification_variable146 =null;


		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:184:5: ( path_expression | identification_variable )
			int alt44=2;
			int LA44_0 = input.LA(1);
			if ( (LA44_0==WORD) ) {
				int LA44_1 = input.LA(2);
				if ( (LA44_1==57) ) {
					alt44=1;
				}
				else if ( (LA44_1==EOF||LA44_1==HAVING||LA44_1==ORDER||LA44_1==RPAREN||LA44_1==55) ) {
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
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:184:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_groupby_item1566);
					path_expression145=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression145.getTree());

					}
					break;
				case 2 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:184:25: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_groupby_item1570);
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:185:1: having_clause : 'HAVING' conditional_expression ;
	public final JPA2Parser.having_clause_return having_clause() throws RecognitionException {
		JPA2Parser.having_clause_return retval = new JPA2Parser.having_clause_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal147=null;
		ParserRuleReturnScope conditional_expression148 =null;

		Object string_literal147_tree=null;

		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:186:5: ( 'HAVING' conditional_expression )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:186:7: 'HAVING' conditional_expression
			{
			root_0 = (Object)adaptor.nil();


			string_literal147=(Token)match(input,HAVING,FOLLOW_HAVING_in_having_clause1581); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal147_tree = (Object)adaptor.create(string_literal147);
			adaptor.addChild(root_0, string_literal147_tree);
			}

			pushFollow(FOLLOW_conditional_expression_in_having_clause1583);
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:187:1: orderby_clause : 'ORDER' 'BY' orderby_item ( ',' orderby_item )* -> ^( T_ORDER_BY[] 'ORDER' 'BY' ( orderby_item )* ) ;
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
		RewriteRuleTokenStream stream_BY=new RewriteRuleTokenStream(adaptor,"token BY");
		RewriteRuleTokenStream stream_55=new RewriteRuleTokenStream(adaptor,"token 55");
		RewriteRuleTokenStream stream_ORDER=new RewriteRuleTokenStream(adaptor,"token ORDER");
		RewriteRuleSubtreeStream stream_orderby_item=new RewriteRuleSubtreeStream(adaptor,"rule orderby_item");

		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:188:5: ( 'ORDER' 'BY' orderby_item ( ',' orderby_item )* -> ^( T_ORDER_BY[] 'ORDER' 'BY' ( orderby_item )* ) )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:188:7: 'ORDER' 'BY' orderby_item ( ',' orderby_item )*
			{
			string_literal149=(Token)match(input,ORDER,FOLLOW_ORDER_in_orderby_clause1594); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_ORDER.add(string_literal149);

			string_literal150=(Token)match(input,BY,FOLLOW_BY_in_orderby_clause1596); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_BY.add(string_literal150);

			pushFollow(FOLLOW_orderby_item_in_orderby_clause1598);
			orderby_item151=orderby_item();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_orderby_item.add(orderby_item151.getTree());
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:188:33: ( ',' orderby_item )*
			loop45:
			while (true) {
				int alt45=2;
				int LA45_0 = input.LA(1);
				if ( (LA45_0==55) ) {
					alt45=1;
				}

				switch (alt45) {
				case 1 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:188:34: ',' orderby_item
					{
					char_literal152=(Token)match(input,55,FOLLOW_55_in_orderby_clause1601); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_55.add(char_literal152);

					pushFollow(FOLLOW_orderby_item_in_orderby_clause1603);
					orderby_item153=orderby_item();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_orderby_item.add(orderby_item153.getTree());
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
			// 189:5: -> ^( T_ORDER_BY[] 'ORDER' 'BY' ( orderby_item )* )
			{
				// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:189:8: ^( T_ORDER_BY[] 'ORDER' 'BY' ( orderby_item )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new OrderByNode(T_ORDER_BY), root_1);
				adaptor.addChild(root_1, stream_ORDER.nextNode());
				adaptor.addChild(root_1, stream_BY.nextNode());
				// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:189:49: ( orderby_item )*
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:190:1: orderby_item : ( orderby_variable ( 'ASC' )? -> ^( T_ORDER_BY_FIELD[] orderby_variable ( 'ASC' )? ) | orderby_variable 'DESC' -> ^( T_ORDER_BY_FIELD[] orderby_variable 'DESC' ) );
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
		RewriteRuleTokenStream stream_DESC=new RewriteRuleTokenStream(adaptor,"token DESC");
		RewriteRuleTokenStream stream_ASC=new RewriteRuleTokenStream(adaptor,"token ASC");
		RewriteRuleSubtreeStream stream_orderby_variable=new RewriteRuleSubtreeStream(adaptor,"rule orderby_variable");

		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:191:5: ( orderby_variable ( 'ASC' )? -> ^( T_ORDER_BY_FIELD[] orderby_variable ( 'ASC' )? ) | orderby_variable 'DESC' -> ^( T_ORDER_BY_FIELD[] orderby_variable 'DESC' ) )
			int alt47=2;
			alt47 = dfa47.predict(input);
			switch (alt47) {
				case 1 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:191:7: orderby_variable ( 'ASC' )?
					{
					pushFollow(FOLLOW_orderby_variable_in_orderby_item1637);
					orderby_variable154=orderby_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_orderby_variable.add(orderby_variable154.getTree());
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:191:24: ( 'ASC' )?
					int alt46=2;
					int LA46_0 = input.LA(1);
					if ( (LA46_0==ASC) ) {
						alt46=1;
					}
					switch (alt46) {
						case 1 :
							// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:191:25: 'ASC'
							{
							string_literal155=(Token)match(input,ASC,FOLLOW_ASC_in_orderby_item1640); if (state.failed) return retval; 
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
					// 192:6: -> ^( T_ORDER_BY_FIELD[] orderby_variable ( 'ASC' )? )
					{
						// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:192:9: ^( T_ORDER_BY_FIELD[] orderby_variable ( 'ASC' )? )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new OrderByFieldNode(T_ORDER_BY_FIELD), root_1);
						adaptor.addChild(root_1, stream_orderby_variable.nextTree());
						// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:192:65: ( 'ASC' )?
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
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:193:7: orderby_variable 'DESC'
					{
					pushFollow(FOLLOW_orderby_variable_in_orderby_item1672);
					orderby_variable156=orderby_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_orderby_variable.add(orderby_variable156.getTree());
					string_literal157=(Token)match(input,DESC,FOLLOW_DESC_in_orderby_item1675); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_DESC.add(string_literal157);

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
					// 194:5: -> ^( T_ORDER_BY_FIELD[] orderby_variable 'DESC' )
					{
						// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:194:8: ^( T_ORDER_BY_FIELD[] orderby_variable 'DESC' )
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:195:1: orderby_variable : ( path_expression | general_identification_variable | result_variable );
	public final JPA2Parser.orderby_variable_return orderby_variable() throws RecognitionException {
		JPA2Parser.orderby_variable_return retval = new JPA2Parser.orderby_variable_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression158 =null;
		ParserRuleReturnScope general_identification_variable159 =null;
		ParserRuleReturnScope result_variable160 =null;


		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:196:5: ( path_expression | general_identification_variable | result_variable )
			int alt48=3;
			int LA48_0 = input.LA(1);
			if ( (LA48_0==WORD) ) {
				int LA48_1 = input.LA(2);
				if ( (LA48_1==57) ) {
					alt48=1;
				}
				else if ( (synpred62_JPA2()) ) {
					alt48=2;
				}
				else if ( (true) ) {
					alt48=3;
				}

			}
			else if ( (LA48_0==98||LA48_0==130) ) {
				alt48=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 48, 0, input);
				throw nvae;
			}

			switch (alt48) {
				case 1 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:196:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_orderby_variable1704);
					path_expression158=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression158.getTree());

					}
					break;
				case 2 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:196:25: general_identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_general_identification_variable_in_orderby_variable1708);
					general_identification_variable159=general_identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, general_identification_variable159.getTree());

					}
					break;
				case 3 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:196:59: result_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_result_variable_in_orderby_variable1712);
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:198:1: subquery : lp= '(' 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? rp= ')' -> ^( T_QUERY[$lp,$rp] 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ) ;
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
		RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
		RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
		RewriteRuleSubtreeStream stream_groupby_clause=new RewriteRuleSubtreeStream(adaptor,"rule groupby_clause");
		RewriteRuleSubtreeStream stream_having_clause=new RewriteRuleSubtreeStream(adaptor,"rule having_clause");
		RewriteRuleSubtreeStream stream_where_clause=new RewriteRuleSubtreeStream(adaptor,"rule where_clause");
		RewriteRuleSubtreeStream stream_subquery_from_clause=new RewriteRuleSubtreeStream(adaptor,"rule subquery_from_clause");
		RewriteRuleSubtreeStream stream_simple_select_clause=new RewriteRuleSubtreeStream(adaptor,"rule simple_select_clause");

		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:199:5: (lp= '(' 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? rp= ')' -> ^( T_QUERY[$lp,$rp] 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? ) )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:199:7: lp= '(' 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? rp= ')'
			{
			lp=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_subquery1726); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_LPAREN.add(lp);

			string_literal161=(Token)match(input,117,FOLLOW_117_in_subquery1728); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_117.add(string_literal161);

			pushFollow(FOLLOW_simple_select_clause_in_subquery1730);
			simple_select_clause162=simple_select_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_simple_select_clause.add(simple_select_clause162.getTree());
			pushFollow(FOLLOW_subquery_from_clause_in_subquery1732);
			subquery_from_clause163=subquery_from_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_subquery_from_clause.add(subquery_from_clause163.getTree());
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:199:65: ( where_clause )?
			int alt49=2;
			int LA49_0 = input.LA(1);
			if ( (LA49_0==132) ) {
				alt49=1;
			}
			switch (alt49) {
				case 1 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:199:66: where_clause
					{
					pushFollow(FOLLOW_where_clause_in_subquery1735);
					where_clause164=where_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_where_clause.add(where_clause164.getTree());
					}
					break;

			}

			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:199:81: ( groupby_clause )?
			int alt50=2;
			int LA50_0 = input.LA(1);
			if ( (LA50_0==GROUP) ) {
				alt50=1;
			}
			switch (alt50) {
				case 1 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:199:82: groupby_clause
					{
					pushFollow(FOLLOW_groupby_clause_in_subquery1740);
					groupby_clause165=groupby_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_groupby_clause.add(groupby_clause165.getTree());
					}
					break;

			}

			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:199:99: ( having_clause )?
			int alt51=2;
			int LA51_0 = input.LA(1);
			if ( (LA51_0==HAVING) ) {
				alt51=1;
			}
			switch (alt51) {
				case 1 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:199:100: having_clause
					{
					pushFollow(FOLLOW_having_clause_in_subquery1745);
					having_clause166=having_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_having_clause.add(having_clause166.getTree());
					}
					break;

			}

			rp=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_subquery1751); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_RPAREN.add(rp);

			// AST REWRITE
			// elements: subquery_from_clause, simple_select_clause, groupby_clause, 117, having_clause, where_clause
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (Object)adaptor.nil();
			// 200:6: -> ^( T_QUERY[$lp,$rp] 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? )
			{
				// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:200:9: ^( T_QUERY[$lp,$rp] 'SELECT' simple_select_clause subquery_from_clause ( where_clause )? ( groupby_clause )? ( having_clause )? )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new QueryNode(T_QUERY, lp, rp), root_1);
				adaptor.addChild(root_1, stream_117.nextNode());
				adaptor.addChild(root_1, stream_simple_select_clause.nextTree());
				adaptor.addChild(root_1, stream_subquery_from_clause.nextTree());
				// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:200:90: ( where_clause )?
				if ( stream_where_clause.hasNext() ) {
					adaptor.addChild(root_1, stream_where_clause.nextTree());
				}
				stream_where_clause.reset();

				// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:200:106: ( groupby_clause )?
				if ( stream_groupby_clause.hasNext() ) {
					adaptor.addChild(root_1, stream_groupby_clause.nextTree());
				}
				stream_groupby_clause.reset();

				// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:200:124: ( having_clause )?
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:201:1: subquery_from_clause : fr= 'FROM' subselect_identification_variable_declaration ( ',' subselect_identification_variable_declaration )* -> ^( T_SOURCES[$fr] ( ^( T_SOURCE subselect_identification_variable_declaration ) )* ) ;
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
		RewriteRuleTokenStream stream_92=new RewriteRuleTokenStream(adaptor,"token 92");
		RewriteRuleTokenStream stream_55=new RewriteRuleTokenStream(adaptor,"token 55");
		RewriteRuleSubtreeStream stream_subselect_identification_variable_declaration=new RewriteRuleSubtreeStream(adaptor,"rule subselect_identification_variable_declaration");

		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:202:5: (fr= 'FROM' subselect_identification_variable_declaration ( ',' subselect_identification_variable_declaration )* -> ^( T_SOURCES[$fr] ( ^( T_SOURCE subselect_identification_variable_declaration ) )* ) )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:202:7: fr= 'FROM' subselect_identification_variable_declaration ( ',' subselect_identification_variable_declaration )*
			{
			fr=(Token)match(input,92,FOLLOW_92_in_subquery_from_clause1801); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_92.add(fr);

			pushFollow(FOLLOW_subselect_identification_variable_declaration_in_subquery_from_clause1803);
			subselect_identification_variable_declaration167=subselect_identification_variable_declaration();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_subselect_identification_variable_declaration.add(subselect_identification_variable_declaration167.getTree());
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:202:63: ( ',' subselect_identification_variable_declaration )*
			loop52:
			while (true) {
				int alt52=2;
				int LA52_0 = input.LA(1);
				if ( (LA52_0==55) ) {
					alt52=1;
				}

				switch (alt52) {
				case 1 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:202:64: ',' subselect_identification_variable_declaration
					{
					char_literal168=(Token)match(input,55,FOLLOW_55_in_subquery_from_clause1806); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_55.add(char_literal168);

					pushFollow(FOLLOW_subselect_identification_variable_declaration_in_subquery_from_clause1808);
					subselect_identification_variable_declaration169=subselect_identification_variable_declaration();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_subselect_identification_variable_declaration.add(subselect_identification_variable_declaration169.getTree());
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
			// 203:5: -> ^( T_SOURCES[$fr] ( ^( T_SOURCE subselect_identification_variable_declaration ) )* )
			{
				// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:203:8: ^( T_SOURCES[$fr] ( ^( T_SOURCE subselect_identification_variable_declaration ) )* )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot(new FromNode(T_SOURCES, fr), root_1);
				// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:203:35: ( ^( T_SOURCE subselect_identification_variable_declaration ) )*
				while ( stream_subselect_identification_variable_declaration.hasNext() ) {
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:203:35: ^( T_SOURCE subselect_identification_variable_declaration )
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:205:1: subselect_identification_variable_declaration : ( identification_variable_declaration | derived_path_expression 'AS' identification_variable ( join )* | derived_collection_member_declaration );
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
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:206:5: ( identification_variable_declaration | derived_path_expression 'AS' identification_variable ( join )* | derived_collection_member_declaration )
			int alt54=3;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA54_1 = input.LA(2);
				if ( (LA54_1==WORD||LA54_1==75) ) {
					alt54=1;
				}
				else if ( (LA54_1==57) ) {
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
			case 125:
				{
				alt54=2;
				}
				break;
			case 95:
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
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:206:7: identification_variable_declaration
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_declaration_in_subselect_identification_variable_declaration1846);
					identification_variable_declaration170=identification_variable_declaration();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable_declaration170.getTree());

					}
					break;
				case 2 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:207:7: derived_path_expression 'AS' identification_variable ( join )*
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_derived_path_expression_in_subselect_identification_variable_declaration1854);
					derived_path_expression171=derived_path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, derived_path_expression171.getTree());

					string_literal172=(Token)match(input,75,FOLLOW_75_in_subselect_identification_variable_declaration1856); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal172_tree = (Object)adaptor.create(string_literal172);
					adaptor.addChild(root_0, string_literal172_tree);
					}

					pushFollow(FOLLOW_identification_variable_in_subselect_identification_variable_declaration1858);
					identification_variable173=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable173.getTree());

					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:207:60: ( join )*
					loop53:
					while (true) {
						int alt53=2;
						int LA53_0 = input.LA(1);
						if ( (LA53_0==INNER||(LA53_0 >= JOIN && LA53_0 <= LEFT)) ) {
							alt53=1;
						}

						switch (alt53) {
						case 1 :
							// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:207:61: join
							{
							pushFollow(FOLLOW_join_in_subselect_identification_variable_declaration1861);
							join174=join();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, join174.getTree());

							}
							break;

						default :
							break loop53;
						}
					}

					}
					break;
				case 3 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:208:7: derived_collection_member_declaration
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_derived_collection_member_declaration_in_subselect_identification_variable_declaration1871);
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:209:1: derived_path_expression : ( general_derived_path '.' single_valued_object_field | general_derived_path '.' collection_valued_field );
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
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:210:5: ( general_derived_path '.' single_valued_object_field | general_derived_path '.' collection_valued_field )
			int alt55=2;
			int LA55_0 = input.LA(1);
			if ( (LA55_0==WORD) ) {
				int LA55_1 = input.LA(2);
				if ( (synpred70_JPA2()) ) {
					alt55=1;
				}
				else if ( (true) ) {
					alt55=2;
				}

			}
			else if ( (LA55_0==125) ) {
				int LA55_2 = input.LA(2);
				if ( (synpred70_JPA2()) ) {
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
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:210:7: general_derived_path '.' single_valued_object_field
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_general_derived_path_in_derived_path_expression1882);
					general_derived_path176=general_derived_path();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, general_derived_path176.getTree());

					char_literal177=(Token)match(input,57,FOLLOW_57_in_derived_path_expression1883); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal177_tree = (Object)adaptor.create(char_literal177);
					adaptor.addChild(root_0, char_literal177_tree);
					}

					pushFollow(FOLLOW_single_valued_object_field_in_derived_path_expression1884);
					single_valued_object_field178=single_valued_object_field();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, single_valued_object_field178.getTree());

					}
					break;
				case 2 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:211:7: general_derived_path '.' collection_valued_field
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_general_derived_path_in_derived_path_expression1892);
					general_derived_path179=general_derived_path();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, general_derived_path179.getTree());

					char_literal180=(Token)match(input,57,FOLLOW_57_in_derived_path_expression1893); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal180_tree = (Object)adaptor.create(char_literal180);
					adaptor.addChild(root_0, char_literal180_tree);
					}

					pushFollow(FOLLOW_collection_valued_field_in_derived_path_expression1894);
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:212:1: general_derived_path : ( simple_derived_path | treated_derived_path ( '.' single_valued_object_field )* );
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
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:213:5: ( simple_derived_path | treated_derived_path ( '.' single_valued_object_field )* )
			int alt57=2;
			int LA57_0 = input.LA(1);
			if ( (LA57_0==WORD) ) {
				alt57=1;
			}
			else if ( (LA57_0==125) ) {
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
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:213:7: simple_derived_path
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_simple_derived_path_in_general_derived_path1905);
					simple_derived_path182=simple_derived_path();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_derived_path182.getTree());

					}
					break;
				case 2 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:214:7: treated_derived_path ( '.' single_valued_object_field )*
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_treated_derived_path_in_general_derived_path1913);
					treated_derived_path183=treated_derived_path();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, treated_derived_path183.getTree());

					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:214:27: ( '.' single_valued_object_field )*
					loop56:
					while (true) {
						int alt56=2;
						int LA56_0 = input.LA(1);
						if ( (LA56_0==57) ) {
							int LA56_1 = input.LA(2);
							if ( (LA56_1==WORD) ) {
								int LA56_3 = input.LA(3);
								if ( (LA56_3==75) ) {
									int LA56_4 = input.LA(4);
									if ( (LA56_4==WORD) ) {
										int LA56_6 = input.LA(5);
										if ( (LA56_6==RPAREN) ) {
											int LA56_7 = input.LA(6);
											if ( (LA56_7==75) ) {
												int LA56_8 = input.LA(7);
												if ( (LA56_8==WORD) ) {
													int LA56_9 = input.LA(8);
													if ( (LA56_9==RPAREN) ) {
														alt56=1;
													}

												}

											}
											else if ( (LA56_7==57) ) {
												alt56=1;
											}

										}

									}

								}
								else if ( (LA56_3==57) ) {
									alt56=1;
								}

							}

						}

						switch (alt56) {
						case 1 :
							// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:214:28: '.' single_valued_object_field
							{
							char_literal184=(Token)match(input,57,FOLLOW_57_in_general_derived_path1915); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							char_literal184_tree = (Object)adaptor.create(char_literal184);
							adaptor.addChild(root_0, char_literal184_tree);
							}

							pushFollow(FOLLOW_single_valued_object_field_in_general_derived_path1916);
							single_valued_object_field185=single_valued_object_field();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, single_valued_object_field185.getTree());

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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:216:1: simple_derived_path : superquery_identification_variable ;
	public final JPA2Parser.simple_derived_path_return simple_derived_path() throws RecognitionException {
		JPA2Parser.simple_derived_path_return retval = new JPA2Parser.simple_derived_path_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope superquery_identification_variable186 =null;


		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:217:5: ( superquery_identification_variable )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:217:7: superquery_identification_variable
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_superquery_identification_variable_in_simple_derived_path1934);
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:219:1: treated_derived_path : 'TREAT(' general_derived_path 'AS' subtype ')' ;
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
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:220:5: ( 'TREAT(' general_derived_path 'AS' subtype ')' )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:220:7: 'TREAT(' general_derived_path 'AS' subtype ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal187=(Token)match(input,125,FOLLOW_125_in_treated_derived_path1951); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal187_tree = (Object)adaptor.create(string_literal187);
			adaptor.addChild(root_0, string_literal187_tree);
			}

			pushFollow(FOLLOW_general_derived_path_in_treated_derived_path1952);
			general_derived_path188=general_derived_path();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, general_derived_path188.getTree());

			string_literal189=(Token)match(input,75,FOLLOW_75_in_treated_derived_path1954); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal189_tree = (Object)adaptor.create(string_literal189);
			adaptor.addChild(root_0, string_literal189_tree);
			}

			pushFollow(FOLLOW_subtype_in_treated_derived_path1956);
			subtype190=subtype();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, subtype190.getTree());

			char_literal191=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_treated_derived_path1958); if (state.failed) return retval;
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:221:1: derived_collection_member_declaration : 'IN' superquery_identification_variable '.' ( single_valued_object_field '.' )* collection_valued_field ;
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
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:222:5: ( 'IN' superquery_identification_variable '.' ( single_valued_object_field '.' )* collection_valued_field )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:222:7: 'IN' superquery_identification_variable '.' ( single_valued_object_field '.' )* collection_valued_field
			{
			root_0 = (Object)adaptor.nil();


			string_literal192=(Token)match(input,95,FOLLOW_95_in_derived_collection_member_declaration1969); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal192_tree = (Object)adaptor.create(string_literal192);
			adaptor.addChild(root_0, string_literal192_tree);
			}

			pushFollow(FOLLOW_superquery_identification_variable_in_derived_collection_member_declaration1971);
			superquery_identification_variable193=superquery_identification_variable();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, superquery_identification_variable193.getTree());

			char_literal194=(Token)match(input,57,FOLLOW_57_in_derived_collection_member_declaration1972); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal194_tree = (Object)adaptor.create(char_literal194);
			adaptor.addChild(root_0, char_literal194_tree);
			}

			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:222:49: ( single_valued_object_field '.' )*
			loop58:
			while (true) {
				int alt58=2;
				int LA58_0 = input.LA(1);
				if ( (LA58_0==WORD) ) {
					int LA58_1 = input.LA(2);
					if ( (LA58_1==57) ) {
						alt58=1;
					}

				}

				switch (alt58) {
				case 1 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:222:50: single_valued_object_field '.'
					{
					pushFollow(FOLLOW_single_valued_object_field_in_derived_collection_member_declaration1974);
					single_valued_object_field195=single_valued_object_field();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, single_valued_object_field195.getTree());

					char_literal196=(Token)match(input,57,FOLLOW_57_in_derived_collection_member_declaration1976); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal196_tree = (Object)adaptor.create(char_literal196);
					adaptor.addChild(root_0, char_literal196_tree);
					}

					}
					break;

				default :
					break loop58;
				}
			}

			pushFollow(FOLLOW_collection_valued_field_in_derived_collection_member_declaration1979);
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:224:1: simple_select_clause : ( 'DISTINCT' )? simple_select_expression -> ^( T_SELECTED_ITEMS ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression ) ) ;
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
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:225:5: ( ( 'DISTINCT' )? simple_select_expression -> ^( T_SELECTED_ITEMS ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression ) ) )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:225:7: ( 'DISTINCT' )? simple_select_expression
			{
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:225:7: ( 'DISTINCT' )?
			int alt59=2;
			int LA59_0 = input.LA(1);
			if ( (LA59_0==DISTINCT) ) {
				alt59=1;
			}
			switch (alt59) {
				case 1 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:225:8: 'DISTINCT'
					{
					string_literal198=(Token)match(input,DISTINCT,FOLLOW_DISTINCT_in_simple_select_clause1992); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_DISTINCT.add(string_literal198);

					}
					break;

			}

			pushFollow(FOLLOW_simple_select_expression_in_simple_select_clause1996);
			simple_select_expression199=simple_select_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_simple_select_expression.add(simple_select_expression199.getTree());
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
			// 226:5: -> ^( T_SELECTED_ITEMS ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression ) )
			{
				// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:226:8: ^( T_SELECTED_ITEMS ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression ) )
				{
				Object root_1 = (Object)adaptor.nil();
				root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(T_SELECTED_ITEMS, "T_SELECTED_ITEMS"), root_1);
				// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:226:27: ^( T_SELECTED_ITEM[] ( 'DISTINCT' )? simple_select_expression )
				{
				Object root_2 = (Object)adaptor.nil();
				root_2 = (Object)adaptor.becomeRoot(new SelectedItemNode(T_SELECTED_ITEM), root_2);
				// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:226:65: ( 'DISTINCT' )?
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:227:1: simple_select_expression : ( path_expression | scalar_expression | aggregate_expression | identification_variable );
	public final JPA2Parser.simple_select_expression_return simple_select_expression() throws RecognitionException {
		JPA2Parser.simple_select_expression_return retval = new JPA2Parser.simple_select_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression200 =null;
		ParserRuleReturnScope scalar_expression201 =null;
		ParserRuleReturnScope aggregate_expression202 =null;
		ParserRuleReturnScope identification_variable203 =null;


		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:228:5: ( path_expression | scalar_expression | aggregate_expression | identification_variable )
			int alt60=4;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA60_1 = input.LA(2);
				if ( (synpred75_JPA2()) ) {
					alt60=1;
				}
				else if ( (synpred76_JPA2()) ) {
					alt60=2;
				}
				else if ( (true) ) {
					alt60=4;
				}

				}
				break;
			case INT_NUMERAL:
			case LPAREN:
			case NAMED_PARAMETER:
			case STRING_LITERAL:
			case 52:
			case 54:
			case 56:
			case 59:
			case 66:
			case 72:
			case 78:
			case 79:
			case 80:
			case 81:
			case 82:
			case 83:
			case 96:
			case 100:
			case 102:
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
				alt60=2;
				}
				break;
			case COUNT:
				{
				int LA60_16 = input.LA(2);
				if ( (synpred76_JPA2()) ) {
					alt60=2;
				}
				else if ( (synpred77_JPA2()) ) {
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
				if ( (synpred76_JPA2()) ) {
					alt60=2;
				}
				else if ( (synpred77_JPA2()) ) {
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
			case 93:
				{
				int LA60_18 = input.LA(2);
				if ( (synpred76_JPA2()) ) {
					alt60=2;
				}
				else if ( (synpred77_JPA2()) ) {
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
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 60, 0, input);
				throw nvae;
			}
			switch (alt60) {
				case 1 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:228:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_simple_select_expression2032);
					path_expression200=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression200.getTree());

					}
					break;
				case 2 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:229:7: scalar_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_scalar_expression_in_simple_select_expression2040);
					scalar_expression201=scalar_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression201.getTree());

					}
					break;
				case 3 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:230:7: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_simple_select_expression2048);
					aggregate_expression202=aggregate_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression202.getTree());

					}
					break;
				case 4 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:231:7: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_simple_select_expression2056);
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:232:1: scalar_expression : ( arithmetic_expression | string_expression | enum_expression | datetime_expression | boolean_expression | case_expression | entity_type_expression );
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
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:233:5: ( arithmetic_expression | string_expression | enum_expression | datetime_expression | boolean_expression | case_expression | entity_type_expression )
			int alt61=7;
			switch ( input.LA(1) ) {
			case INT_NUMERAL:
			case 54:
			case 56:
			case 59:
			case 72:
			case 96:
			case 100:
			case 102:
			case 106:
			case 119:
			case 121:
				{
				alt61=1;
				}
				break;
			case WORD:
				{
				int LA61_2 = input.LA(2);
				if ( (synpred78_JPA2()) ) {
					alt61=1;
				}
				else if ( (synpred79_JPA2()) ) {
					alt61=2;
				}
				else if ( (synpred80_JPA2()) ) {
					alt61=3;
				}
				else if ( (synpred81_JPA2()) ) {
					alt61=4;
				}
				else if ( (synpred82_JPA2()) ) {
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
				if ( (synpred78_JPA2()) ) {
					alt61=1;
				}
				else if ( (synpred79_JPA2()) ) {
					alt61=2;
				}
				else if ( (synpred80_JPA2()) ) {
					alt61=3;
				}
				else if ( (synpred81_JPA2()) ) {
					alt61=4;
				}
				else if ( (synpred82_JPA2()) ) {
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
			case 66:
				{
				int LA61_6 = input.LA(2);
				if ( (synpred78_JPA2()) ) {
					alt61=1;
				}
				else if ( (synpred79_JPA2()) ) {
					alt61=2;
				}
				else if ( (synpred80_JPA2()) ) {
					alt61=3;
				}
				else if ( (synpred81_JPA2()) ) {
					alt61=4;
				}
				else if ( (synpred82_JPA2()) ) {
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
				if ( (synpred78_JPA2()) ) {
					alt61=1;
				}
				else if ( (synpred79_JPA2()) ) {
					alt61=2;
				}
				else if ( (synpred80_JPA2()) ) {
					alt61=3;
				}
				else if ( (synpred81_JPA2()) ) {
					alt61=4;
				}
				else if ( (synpred82_JPA2()) ) {
					alt61=5;
				}
				else if ( (true) ) {
					alt61=7;
				}

				}
				break;
			case 52:
				{
				int LA61_8 = input.LA(2);
				if ( (synpred78_JPA2()) ) {
					alt61=1;
				}
				else if ( (synpred79_JPA2()) ) {
					alt61=2;
				}
				else if ( (synpred80_JPA2()) ) {
					alt61=3;
				}
				else if ( (synpred81_JPA2()) ) {
					alt61=4;
				}
				else if ( (synpred82_JPA2()) ) {
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
				if ( (synpred78_JPA2()) ) {
					alt61=1;
				}
				else if ( (synpred79_JPA2()) ) {
					alt61=2;
				}
				else if ( (synpred81_JPA2()) ) {
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
				if ( (synpred78_JPA2()) ) {
					alt61=1;
				}
				else if ( (synpred79_JPA2()) ) {
					alt61=2;
				}
				else if ( (synpred81_JPA2()) ) {
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
			case 93:
				{
				int LA61_18 = input.LA(2);
				if ( (synpred78_JPA2()) ) {
					alt61=1;
				}
				else if ( (synpred79_JPA2()) ) {
					alt61=2;
				}
				else if ( (synpred81_JPA2()) ) {
					alt61=4;
				}
				else if ( (synpred82_JPA2()) ) {
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
			case 78:
				{
				int LA61_19 = input.LA(2);
				if ( (synpred78_JPA2()) ) {
					alt61=1;
				}
				else if ( (synpred79_JPA2()) ) {
					alt61=2;
				}
				else if ( (synpred80_JPA2()) ) {
					alt61=3;
				}
				else if ( (synpred81_JPA2()) ) {
					alt61=4;
				}
				else if ( (synpred82_JPA2()) ) {
					alt61=5;
				}
				else if ( (synpred83_JPA2()) ) {
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
			case 79:
				{
				int LA61_20 = input.LA(2);
				if ( (synpred78_JPA2()) ) {
					alt61=1;
				}
				else if ( (synpred79_JPA2()) ) {
					alt61=2;
				}
				else if ( (synpred80_JPA2()) ) {
					alt61=3;
				}
				else if ( (synpred81_JPA2()) ) {
					alt61=4;
				}
				else if ( (synpred82_JPA2()) ) {
					alt61=5;
				}
				else if ( (synpred83_JPA2()) ) {
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
			case 112:
				{
				int LA61_21 = input.LA(2);
				if ( (synpred78_JPA2()) ) {
					alt61=1;
				}
				else if ( (synpred79_JPA2()) ) {
					alt61=2;
				}
				else if ( (synpred80_JPA2()) ) {
					alt61=3;
				}
				else if ( (synpred81_JPA2()) ) {
					alt61=4;
				}
				else if ( (synpred82_JPA2()) ) {
					alt61=5;
				}
				else if ( (synpred83_JPA2()) ) {
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
			case STRING_LITERAL:
			case 80:
			case 103:
			case 122:
			case 126:
			case 129:
				{
				alt61=2;
				}
				break;
			case 81:
			case 82:
			case 83:
				{
				alt61=4;
				}
				break;
			case 134:
			case 135:
				{
				alt61=5;
				}
				break;
			case 127:
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
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:233:7: arithmetic_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_expression_in_scalar_expression2067);
					arithmetic_expression204=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression204.getTree());

					}
					break;
				case 2 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:234:7: string_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_string_expression_in_scalar_expression2075);
					string_expression205=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression205.getTree());

					}
					break;
				case 3 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:235:7: enum_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_enum_expression_in_scalar_expression2083);
					enum_expression206=enum_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_expression206.getTree());

					}
					break;
				case 4 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:236:7: datetime_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_datetime_expression_in_scalar_expression2091);
					datetime_expression207=datetime_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression207.getTree());

					}
					break;
				case 5 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:237:7: boolean_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_boolean_expression_in_scalar_expression2099);
					boolean_expression208=boolean_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_expression208.getTree());

					}
					break;
				case 6 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:238:7: case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_case_expression_in_scalar_expression2107);
					case_expression209=case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, case_expression209.getTree());

					}
					break;
				case 7 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:239:7: entity_type_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_entity_type_expression_in_scalar_expression2115);
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:240:1: conditional_expression : ( conditional_term ) ( 'OR' conditional_term )* ;
	public final JPA2Parser.conditional_expression_return conditional_expression() throws RecognitionException {
		JPA2Parser.conditional_expression_return retval = new JPA2Parser.conditional_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal212=null;
		ParserRuleReturnScope conditional_term211 =null;
		ParserRuleReturnScope conditional_term213 =null;

		Object string_literal212_tree=null;

		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:241:5: ( ( conditional_term ) ( 'OR' conditional_term )* )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:241:7: ( conditional_term ) ( 'OR' conditional_term )*
			{
			root_0 = (Object)adaptor.nil();


			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:241:7: ( conditional_term )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:241:8: conditional_term
			{
			pushFollow(FOLLOW_conditional_term_in_conditional_expression2127);
			conditional_term211=conditional_term();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_term211.getTree());

			}

			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:241:26: ( 'OR' conditional_term )*
			loop62:
			while (true) {
				int alt62=2;
				int LA62_0 = input.LA(1);
				if ( (LA62_0==OR) ) {
					alt62=1;
				}

				switch (alt62) {
				case 1 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:241:27: 'OR' conditional_term
					{
					string_literal212=(Token)match(input,OR,FOLLOW_OR_in_conditional_expression2131); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal212_tree = (Object)adaptor.create(string_literal212);
					adaptor.addChild(root_0, string_literal212_tree);
					}

					pushFollow(FOLLOW_conditional_term_in_conditional_expression2133);
					conditional_term213=conditional_term();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_term213.getTree());

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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:242:1: conditional_term : ( conditional_factor ) ( 'AND' conditional_factor )* ;
	public final JPA2Parser.conditional_term_return conditional_term() throws RecognitionException {
		JPA2Parser.conditional_term_return retval = new JPA2Parser.conditional_term_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal215=null;
		ParserRuleReturnScope conditional_factor214 =null;
		ParserRuleReturnScope conditional_factor216 =null;

		Object string_literal215_tree=null;

		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:243:5: ( ( conditional_factor ) ( 'AND' conditional_factor )* )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:243:7: ( conditional_factor ) ( 'AND' conditional_factor )*
			{
			root_0 = (Object)adaptor.nil();


			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:243:7: ( conditional_factor )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:243:8: conditional_factor
			{
			pushFollow(FOLLOW_conditional_factor_in_conditional_term2147);
			conditional_factor214=conditional_factor();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_factor214.getTree());

			}

			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:243:28: ( 'AND' conditional_factor )*
			loop63:
			while (true) {
				int alt63=2;
				int LA63_0 = input.LA(1);
				if ( (LA63_0==AND) ) {
					alt63=1;
				}

				switch (alt63) {
				case 1 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:243:29: 'AND' conditional_factor
					{
					string_literal215=(Token)match(input,AND,FOLLOW_AND_in_conditional_term2151); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal215_tree = (Object)adaptor.create(string_literal215);
					adaptor.addChild(root_0, string_literal215_tree);
					}

					pushFollow(FOLLOW_conditional_factor_in_conditional_term2153);
					conditional_factor216=conditional_factor();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_factor216.getTree());

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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:244:1: conditional_factor : ( 'NOT' )? conditional_primary ;
	public final JPA2Parser.conditional_factor_return conditional_factor() throws RecognitionException {
		JPA2Parser.conditional_factor_return retval = new JPA2Parser.conditional_factor_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal217=null;
		ParserRuleReturnScope conditional_primary218 =null;

		Object string_literal217_tree=null;

		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:245:5: ( ( 'NOT' )? conditional_primary )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:245:7: ( 'NOT' )? conditional_primary
			{
			root_0 = (Object)adaptor.nil();


			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:245:7: ( 'NOT' )?
			int alt64=2;
			int LA64_0 = input.LA(1);
			if ( (LA64_0==109) ) {
				int LA64_1 = input.LA(2);
				if ( (synpred86_JPA2()) ) {
					alt64=1;
				}
			}
			switch (alt64) {
				case 1 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:245:8: 'NOT'
					{
					string_literal217=(Token)match(input,109,FOLLOW_109_in_conditional_factor2167); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal217_tree = (Object)adaptor.create(string_literal217);
					adaptor.addChild(root_0, string_literal217_tree);
					}

					}
					break;

			}

			pushFollow(FOLLOW_conditional_primary_in_conditional_factor2171);
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:246:1: conditional_primary : ( simple_cond_expression -> ^( T_SIMPLE_CONDITION[] simple_cond_expression ) | '(' conditional_expression ')' );
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
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:247:5: ( simple_cond_expression -> ^( T_SIMPLE_CONDITION[] simple_cond_expression ) | '(' conditional_expression ')' )
			int alt65=2;
			int LA65_0 = input.LA(1);
			if ( (LA65_0==AVG||LA65_0==COUNT||LA65_0==INT_NUMERAL||(LA65_0 >= MAX && LA65_0 <= NAMED_PARAMETER)||(LA65_0 >= STRING_LITERAL && LA65_0 <= SUM)||LA65_0==WORD||LA65_0==52||LA65_0==54||LA65_0==56||LA65_0==59||(LA65_0 >= 66 && LA65_0 <= 72)||(LA65_0 >= 78 && LA65_0 <= 83)||LA65_0==91||LA65_0==93||LA65_0==96||LA65_0==100||(LA65_0 >= 102 && LA65_0 <= 103)||LA65_0==106||LA65_0==109||LA65_0==112||LA65_0==119||(LA65_0 >= 121 && LA65_0 <= 122)||(LA65_0 >= 126 && LA65_0 <= 127)||LA65_0==129||(LA65_0 >= 134 && LA65_0 <= 135)) ) {
				alt65=1;
			}
			else if ( (LA65_0==LPAREN) ) {
				int LA65_17 = input.LA(2);
				if ( (synpred87_JPA2()) ) {
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
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:247:7: simple_cond_expression
					{
					pushFollow(FOLLOW_simple_cond_expression_in_conditional_primary2182);
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
					// 248:5: -> ^( T_SIMPLE_CONDITION[] simple_cond_expression )
					{
						// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:248:8: ^( T_SIMPLE_CONDITION[] simple_cond_expression )
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
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:249:7: '(' conditional_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					char_literal220=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_conditional_primary2206); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal220_tree = (Object)adaptor.create(char_literal220);
					adaptor.addChild(root_0, char_literal220_tree);
					}

					pushFollow(FOLLOW_conditional_expression_in_conditional_primary2207);
					conditional_expression221=conditional_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_expression221.getTree());

					char_literal222=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_conditional_primary2208); if (state.failed) return retval;
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:250:1: simple_cond_expression : ( comparison_expression | between_expression | in_expression | like_expression | null_comparison_expression | empty_collection_comparison_expression | collection_member_expression | exists_expression | date_macro_expression );
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
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:251:5: ( comparison_expression | between_expression | in_expression | like_expression | null_comparison_expression | empty_collection_comparison_expression | collection_member_expression | exists_expression | date_macro_expression )
			int alt66=9;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA66_1 = input.LA(2);
				if ( (synpred88_JPA2()) ) {
					alt66=1;
				}
				else if ( (synpred89_JPA2()) ) {
					alt66=2;
				}
				else if ( (synpred90_JPA2()) ) {
					alt66=3;
				}
				else if ( (synpred91_JPA2()) ) {
					alt66=4;
				}
				else if ( (synpred92_JPA2()) ) {
					alt66=5;
				}
				else if ( (synpred93_JPA2()) ) {
					alt66=6;
				}
				else if ( (synpred94_JPA2()) ) {
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
				if ( (synpred88_JPA2()) ) {
					alt66=1;
				}
				else if ( (synpred89_JPA2()) ) {
					alt66=2;
				}
				else if ( (synpred91_JPA2()) ) {
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
			case 66:
				{
				int LA66_3 = input.LA(2);
				if ( (synpred88_JPA2()) ) {
					alt66=1;
				}
				else if ( (synpred89_JPA2()) ) {
					alt66=2;
				}
				else if ( (synpred91_JPA2()) ) {
					alt66=4;
				}
				else if ( (synpred92_JPA2()) ) {
					alt66=5;
				}
				else if ( (synpred94_JPA2()) ) {
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
				if ( (synpred88_JPA2()) ) {
					alt66=1;
				}
				else if ( (synpred89_JPA2()) ) {
					alt66=2;
				}
				else if ( (synpred91_JPA2()) ) {
					alt66=4;
				}
				else if ( (synpred92_JPA2()) ) {
					alt66=5;
				}
				else if ( (synpred94_JPA2()) ) {
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
			case 52:
				{
				int LA66_5 = input.LA(2);
				if ( (synpred88_JPA2()) ) {
					alt66=1;
				}
				else if ( (synpred89_JPA2()) ) {
					alt66=2;
				}
				else if ( (synpred91_JPA2()) ) {
					alt66=4;
				}
				else if ( (synpred92_JPA2()) ) {
					alt66=5;
				}
				else if ( (synpred94_JPA2()) ) {
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
			case 80:
				{
				int LA66_6 = input.LA(2);
				if ( (synpred88_JPA2()) ) {
					alt66=1;
				}
				else if ( (synpred89_JPA2()) ) {
					alt66=2;
				}
				else if ( (synpred91_JPA2()) ) {
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
			case 122:
				{
				int LA66_7 = input.LA(2);
				if ( (synpred88_JPA2()) ) {
					alt66=1;
				}
				else if ( (synpred89_JPA2()) ) {
					alt66=2;
				}
				else if ( (synpred91_JPA2()) ) {
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
			case 126:
				{
				int LA66_8 = input.LA(2);
				if ( (synpred88_JPA2()) ) {
					alt66=1;
				}
				else if ( (synpred89_JPA2()) ) {
					alt66=2;
				}
				else if ( (synpred91_JPA2()) ) {
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
			case 103:
				{
				int LA66_9 = input.LA(2);
				if ( (synpred88_JPA2()) ) {
					alt66=1;
				}
				else if ( (synpred89_JPA2()) ) {
					alt66=2;
				}
				else if ( (synpred91_JPA2()) ) {
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
			case 129:
				{
				int LA66_10 = input.LA(2);
				if ( (synpred88_JPA2()) ) {
					alt66=1;
				}
				else if ( (synpred89_JPA2()) ) {
					alt66=2;
				}
				else if ( (synpred91_JPA2()) ) {
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
				if ( (synpred88_JPA2()) ) {
					alt66=1;
				}
				else if ( (synpred89_JPA2()) ) {
					alt66=2;
				}
				else if ( (synpred91_JPA2()) ) {
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
				if ( (synpred88_JPA2()) ) {
					alt66=1;
				}
				else if ( (synpred89_JPA2()) ) {
					alt66=2;
				}
				else if ( (synpred91_JPA2()) ) {
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
			case 93:
				{
				int LA66_13 = input.LA(2);
				if ( (synpred88_JPA2()) ) {
					alt66=1;
				}
				else if ( (synpred89_JPA2()) ) {
					alt66=2;
				}
				else if ( (synpred91_JPA2()) ) {
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
			case 78:
				{
				int LA66_14 = input.LA(2);
				if ( (synpred88_JPA2()) ) {
					alt66=1;
				}
				else if ( (synpred89_JPA2()) ) {
					alt66=2;
				}
				else if ( (synpred91_JPA2()) ) {
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
			case 79:
				{
				int LA66_15 = input.LA(2);
				if ( (synpred88_JPA2()) ) {
					alt66=1;
				}
				else if ( (synpred89_JPA2()) ) {
					alt66=2;
				}
				else if ( (synpred91_JPA2()) ) {
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
			case 112:
				{
				int LA66_16 = input.LA(2);
				if ( (synpred88_JPA2()) ) {
					alt66=1;
				}
				else if ( (synpred89_JPA2()) ) {
					alt66=2;
				}
				else if ( (synpred91_JPA2()) ) {
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
			case LPAREN:
				{
				int LA66_17 = input.LA(2);
				if ( (synpred88_JPA2()) ) {
					alt66=1;
				}
				else if ( (synpred89_JPA2()) ) {
					alt66=2;
				}
				else if ( (synpred91_JPA2()) ) {
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
			case 134:
			case 135:
				{
				alt66=1;
				}
				break;
			case 81:
			case 82:
			case 83:
				{
				int LA66_19 = input.LA(2);
				if ( (synpred88_JPA2()) ) {
					alt66=1;
				}
				else if ( (synpred89_JPA2()) ) {
					alt66=2;
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
			case 127:
				{
				int LA66_20 = input.LA(2);
				if ( (synpred88_JPA2()) ) {
					alt66=1;
				}
				else if ( (synpred90_JPA2()) ) {
					alt66=3;
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
			case 54:
			case 56:
				{
				int LA66_21 = input.LA(2);
				if ( (synpred88_JPA2()) ) {
					alt66=1;
				}
				else if ( (synpred89_JPA2()) ) {
					alt66=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 66, 21, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 59:
				{
				int LA66_22 = input.LA(2);
				if ( (synpred88_JPA2()) ) {
					alt66=1;
				}
				else if ( (synpred89_JPA2()) ) {
					alt66=2;
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
			case INT_NUMERAL:
				{
				int LA66_23 = input.LA(2);
				if ( (synpred88_JPA2()) ) {
					alt66=1;
				}
				else if ( (synpred89_JPA2()) ) {
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
			case 100:
				{
				int LA66_24 = input.LA(2);
				if ( (synpred88_JPA2()) ) {
					alt66=1;
				}
				else if ( (synpred89_JPA2()) ) {
					alt66=2;
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
			case 102:
				{
				int LA66_25 = input.LA(2);
				if ( (synpred88_JPA2()) ) {
					alt66=1;
				}
				else if ( (synpred89_JPA2()) ) {
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
			case 72:
				{
				int LA66_26 = input.LA(2);
				if ( (synpred88_JPA2()) ) {
					alt66=1;
				}
				else if ( (synpred89_JPA2()) ) {
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
			case 121:
				{
				int LA66_27 = input.LA(2);
				if ( (synpred88_JPA2()) ) {
					alt66=1;
				}
				else if ( (synpred89_JPA2()) ) {
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
			case 106:
				{
				int LA66_28 = input.LA(2);
				if ( (synpred88_JPA2()) ) {
					alt66=1;
				}
				else if ( (synpred89_JPA2()) ) {
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
			case 119:
				{
				int LA66_29 = input.LA(2);
				if ( (synpred88_JPA2()) ) {
					alt66=1;
				}
				else if ( (synpred89_JPA2()) ) {
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
			case 96:
				{
				int LA66_30 = input.LA(2);
				if ( (synpred88_JPA2()) ) {
					alt66=1;
				}
				else if ( (synpred89_JPA2()) ) {
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
			case 91:
			case 109:
				{
				alt66=8;
				}
				break;
			case 67:
			case 68:
			case 69:
			case 70:
			case 71:
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
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:251:7: comparison_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_comparison_expression_in_simple_cond_expression2219);
					comparison_expression223=comparison_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_expression223.getTree());

					}
					break;
				case 2 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:252:7: between_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_between_expression_in_simple_cond_expression2227);
					between_expression224=between_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, between_expression224.getTree());

					}
					break;
				case 3 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:253:7: in_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_in_expression_in_simple_cond_expression2235);
					in_expression225=in_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, in_expression225.getTree());

					}
					break;
				case 4 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:254:7: like_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_like_expression_in_simple_cond_expression2243);
					like_expression226=like_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, like_expression226.getTree());

					}
					break;
				case 5 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:255:7: null_comparison_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_null_comparison_expression_in_simple_cond_expression2251);
					null_comparison_expression227=null_comparison_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, null_comparison_expression227.getTree());

					}
					break;
				case 6 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:256:7: empty_collection_comparison_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_empty_collection_comparison_expression_in_simple_cond_expression2259);
					empty_collection_comparison_expression228=empty_collection_comparison_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, empty_collection_comparison_expression228.getTree());

					}
					break;
				case 7 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:257:7: collection_member_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_collection_member_expression_in_simple_cond_expression2267);
					collection_member_expression229=collection_member_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, collection_member_expression229.getTree());

					}
					break;
				case 8 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:258:7: exists_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_exists_expression_in_simple_cond_expression2275);
					exists_expression230=exists_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, exists_expression230.getTree());

					}
					break;
				case 9 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:259:7: date_macro_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_macro_expression_in_simple_cond_expression2283);
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:262:1: date_macro_expression : ( date_between_macro_expression | date_before_macro_expression | date_after_macro_expression | date_equals_macro_expression | date_today_macro_expression );
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
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:263:5: ( date_between_macro_expression | date_before_macro_expression | date_after_macro_expression | date_equals_macro_expression | date_today_macro_expression )
			int alt67=5;
			switch ( input.LA(1) ) {
			case 67:
				{
				alt67=1;
				}
				break;
			case 69:
				{
				alt67=2;
				}
				break;
			case 68:
				{
				alt67=3;
				}
				break;
			case 70:
				{
				alt67=4;
				}
				break;
			case 71:
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
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:263:7: date_between_macro_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_between_macro_expression_in_date_macro_expression2296);
					date_between_macro_expression232=date_between_macro_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_between_macro_expression232.getTree());

					}
					break;
				case 2 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:264:7: date_before_macro_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_before_macro_expression_in_date_macro_expression2304);
					date_before_macro_expression233=date_before_macro_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_before_macro_expression233.getTree());

					}
					break;
				case 3 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:265:7: date_after_macro_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_after_macro_expression_in_date_macro_expression2312);
					date_after_macro_expression234=date_after_macro_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_after_macro_expression234.getTree());

					}
					break;
				case 4 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:266:7: date_equals_macro_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_equals_macro_expression_in_date_macro_expression2320);
					date_equals_macro_expression235=date_equals_macro_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_equals_macro_expression235.getTree());

					}
					break;
				case 5 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:267:7: date_today_macro_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_today_macro_expression_in_date_macro_expression2328);
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:269:1: date_between_macro_expression : '@BETWEEN' '(' path_expression ',' 'NOW' ( ( '+' | '-' ) numeric_literal )? ',' 'NOW' ( ( '+' | '-' ) numeric_literal )? ',' ( 'YEAR' | 'MONTH' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' ) ')' ;
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
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:270:5: ( '@BETWEEN' '(' path_expression ',' 'NOW' ( ( '+' | '-' ) numeric_literal )? ',' 'NOW' ( ( '+' | '-' ) numeric_literal )? ',' ( 'YEAR' | 'MONTH' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' ) ')' )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:270:7: '@BETWEEN' '(' path_expression ',' 'NOW' ( ( '+' | '-' ) numeric_literal )? ',' 'NOW' ( ( '+' | '-' ) numeric_literal )? ',' ( 'YEAR' | 'MONTH' | 'DAY' | 'HOUR' | 'MINUTE' | 'SECOND' ) ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal237=(Token)match(input,67,FOLLOW_67_in_date_between_macro_expression2340); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal237_tree = (Object)adaptor.create(string_literal237);
			adaptor.addChild(root_0, string_literal237_tree);
			}

			char_literal238=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_between_macro_expression2342); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal238_tree = (Object)adaptor.create(char_literal238);
			adaptor.addChild(root_0, char_literal238_tree);
			}

			pushFollow(FOLLOW_path_expression_in_date_between_macro_expression2344);
			path_expression239=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression239.getTree());

			char_literal240=(Token)match(input,55,FOLLOW_55_in_date_between_macro_expression2346); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal240_tree = (Object)adaptor.create(char_literal240);
			adaptor.addChild(root_0, char_literal240_tree);
			}

			string_literal241=(Token)match(input,110,FOLLOW_110_in_date_between_macro_expression2348); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal241_tree = (Object)adaptor.create(string_literal241);
			adaptor.addChild(root_0, string_literal241_tree);
			}

			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:270:48: ( ( '+' | '-' ) numeric_literal )?
			int alt68=2;
			int LA68_0 = input.LA(1);
			if ( (LA68_0==54||LA68_0==56) ) {
				alt68=1;
			}
			switch (alt68) {
				case 1 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:270:49: ( '+' | '-' ) numeric_literal
					{
					set242=input.LT(1);
					if ( input.LA(1)==54||input.LA(1)==56 ) {
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
					pushFollow(FOLLOW_numeric_literal_in_date_between_macro_expression2359);
					numeric_literal243=numeric_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, numeric_literal243.getTree());

					}
					break;

			}

			char_literal244=(Token)match(input,55,FOLLOW_55_in_date_between_macro_expression2363); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal244_tree = (Object)adaptor.create(char_literal244);
			adaptor.addChild(root_0, char_literal244_tree);
			}

			string_literal245=(Token)match(input,110,FOLLOW_110_in_date_between_macro_expression2365); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal245_tree = (Object)adaptor.create(string_literal245);
			adaptor.addChild(root_0, string_literal245_tree);
			}

			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:270:89: ( ( '+' | '-' ) numeric_literal )?
			int alt69=2;
			int LA69_0 = input.LA(1);
			if ( (LA69_0==54||LA69_0==56) ) {
				alt69=1;
			}
			switch (alt69) {
				case 1 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:270:90: ( '+' | '-' ) numeric_literal
					{
					set246=input.LT(1);
					if ( input.LA(1)==54||input.LA(1)==56 ) {
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
					pushFollow(FOLLOW_numeric_literal_in_date_between_macro_expression2376);
					numeric_literal247=numeric_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, numeric_literal247.getTree());

					}
					break;

			}

			char_literal248=(Token)match(input,55,FOLLOW_55_in_date_between_macro_expression2380); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal248_tree = (Object)adaptor.create(char_literal248);
			adaptor.addChild(root_0, char_literal248_tree);
			}

			set249=input.LT(1);
			if ( input.LA(1)==84||input.LA(1)==94||input.LA(1)==105||input.LA(1)==107||input.LA(1)==116||input.LA(1)==133 ) {
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
			char_literal250=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_between_macro_expression2405); if (state.failed) return retval;
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:272:1: date_before_macro_expression : '@DATEBEFORE' '(' path_expression ',' ( path_expression | input_parameter ) ')' ;
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
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:273:5: ( '@DATEBEFORE' '(' path_expression ',' ( path_expression | input_parameter ) ')' )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:273:7: '@DATEBEFORE' '(' path_expression ',' ( path_expression | input_parameter ) ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal251=(Token)match(input,69,FOLLOW_69_in_date_before_macro_expression2417); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal251_tree = (Object)adaptor.create(string_literal251);
			adaptor.addChild(root_0, string_literal251_tree);
			}

			char_literal252=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_before_macro_expression2419); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal252_tree = (Object)adaptor.create(char_literal252);
			adaptor.addChild(root_0, char_literal252_tree);
			}

			pushFollow(FOLLOW_path_expression_in_date_before_macro_expression2421);
			path_expression253=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression253.getTree());

			char_literal254=(Token)match(input,55,FOLLOW_55_in_date_before_macro_expression2423); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal254_tree = (Object)adaptor.create(char_literal254);
			adaptor.addChild(root_0, char_literal254_tree);
			}

			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:273:45: ( path_expression | input_parameter )
			int alt70=2;
			int LA70_0 = input.LA(1);
			if ( (LA70_0==WORD) ) {
				alt70=1;
			}
			else if ( (LA70_0==NAMED_PARAMETER||LA70_0==52||LA70_0==66) ) {
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
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:273:46: path_expression
					{
					pushFollow(FOLLOW_path_expression_in_date_before_macro_expression2426);
					path_expression255=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression255.getTree());

					}
					break;
				case 2 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:273:64: input_parameter
					{
					pushFollow(FOLLOW_input_parameter_in_date_before_macro_expression2430);
					input_parameter256=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter256.getTree());

					}
					break;

			}

			char_literal257=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_before_macro_expression2433); if (state.failed) return retval;
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:275:1: date_after_macro_expression : '@DATEAFTER' '(' path_expression ',' ( path_expression | input_parameter ) ')' ;
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
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:276:5: ( '@DATEAFTER' '(' path_expression ',' ( path_expression | input_parameter ) ')' )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:276:7: '@DATEAFTER' '(' path_expression ',' ( path_expression | input_parameter ) ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal258=(Token)match(input,68,FOLLOW_68_in_date_after_macro_expression2445); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal258_tree = (Object)adaptor.create(string_literal258);
			adaptor.addChild(root_0, string_literal258_tree);
			}

			char_literal259=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_after_macro_expression2447); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal259_tree = (Object)adaptor.create(char_literal259);
			adaptor.addChild(root_0, char_literal259_tree);
			}

			pushFollow(FOLLOW_path_expression_in_date_after_macro_expression2449);
			path_expression260=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression260.getTree());

			char_literal261=(Token)match(input,55,FOLLOW_55_in_date_after_macro_expression2451); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal261_tree = (Object)adaptor.create(char_literal261);
			adaptor.addChild(root_0, char_literal261_tree);
			}

			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:276:44: ( path_expression | input_parameter )
			int alt71=2;
			int LA71_0 = input.LA(1);
			if ( (LA71_0==WORD) ) {
				alt71=1;
			}
			else if ( (LA71_0==NAMED_PARAMETER||LA71_0==52||LA71_0==66) ) {
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
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:276:45: path_expression
					{
					pushFollow(FOLLOW_path_expression_in_date_after_macro_expression2454);
					path_expression262=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression262.getTree());

					}
					break;
				case 2 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:276:63: input_parameter
					{
					pushFollow(FOLLOW_input_parameter_in_date_after_macro_expression2458);
					input_parameter263=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter263.getTree());

					}
					break;

			}

			char_literal264=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_after_macro_expression2461); if (state.failed) return retval;
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:278:1: date_equals_macro_expression : '@DATEEQUALS' '(' path_expression ',' ( path_expression | input_parameter ) ')' ;
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
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:279:5: ( '@DATEEQUALS' '(' path_expression ',' ( path_expression | input_parameter ) ')' )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:279:7: '@DATEEQUALS' '(' path_expression ',' ( path_expression | input_parameter ) ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal265=(Token)match(input,70,FOLLOW_70_in_date_equals_macro_expression2473); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal265_tree = (Object)adaptor.create(string_literal265);
			adaptor.addChild(root_0, string_literal265_tree);
			}

			char_literal266=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_equals_macro_expression2475); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal266_tree = (Object)adaptor.create(char_literal266);
			adaptor.addChild(root_0, char_literal266_tree);
			}

			pushFollow(FOLLOW_path_expression_in_date_equals_macro_expression2477);
			path_expression267=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression267.getTree());

			char_literal268=(Token)match(input,55,FOLLOW_55_in_date_equals_macro_expression2479); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal268_tree = (Object)adaptor.create(char_literal268);
			adaptor.addChild(root_0, char_literal268_tree);
			}

			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:279:45: ( path_expression | input_parameter )
			int alt72=2;
			int LA72_0 = input.LA(1);
			if ( (LA72_0==WORD) ) {
				alt72=1;
			}
			else if ( (LA72_0==NAMED_PARAMETER||LA72_0==52||LA72_0==66) ) {
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
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:279:46: path_expression
					{
					pushFollow(FOLLOW_path_expression_in_date_equals_macro_expression2482);
					path_expression269=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression269.getTree());

					}
					break;
				case 2 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:279:64: input_parameter
					{
					pushFollow(FOLLOW_input_parameter_in_date_equals_macro_expression2486);
					input_parameter270=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter270.getTree());

					}
					break;

			}

			char_literal271=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_equals_macro_expression2489); if (state.failed) return retval;
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:281:1: date_today_macro_expression : '@TODAY' '(' path_expression ')' ;
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
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:282:5: ( '@TODAY' '(' path_expression ')' )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:282:7: '@TODAY' '(' path_expression ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal272=(Token)match(input,71,FOLLOW_71_in_date_today_macro_expression2501); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal272_tree = (Object)adaptor.create(string_literal272);
			adaptor.addChild(root_0, string_literal272_tree);
			}

			char_literal273=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_date_today_macro_expression2503); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal273_tree = (Object)adaptor.create(char_literal273);
			adaptor.addChild(root_0, char_literal273_tree);
			}

			pushFollow(FOLLOW_path_expression_in_date_today_macro_expression2505);
			path_expression274=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression274.getTree());

			char_literal275=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_date_today_macro_expression2507); if (state.failed) return retval;
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:285:1: between_expression : ( arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression | string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression | datetime_expression ( 'NOT' )? 'BETWEEN' datetime_expression 'AND' datetime_expression );
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
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:286:5: ( arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression | string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression | datetime_expression ( 'NOT' )? 'BETWEEN' datetime_expression 'AND' datetime_expression )
			int alt76=3;
			switch ( input.LA(1) ) {
			case INT_NUMERAL:
			case 54:
			case 56:
			case 59:
			case 72:
			case 96:
			case 100:
			case 102:
			case 106:
			case 119:
			case 121:
				{
				alt76=1;
				}
				break;
			case WORD:
				{
				int LA76_2 = input.LA(2);
				if ( (synpred113_JPA2()) ) {
					alt76=1;
				}
				else if ( (synpred115_JPA2()) ) {
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
				if ( (synpred113_JPA2()) ) {
					alt76=1;
				}
				else if ( (synpred115_JPA2()) ) {
					alt76=2;
				}
				else if ( (true) ) {
					alt76=3;
				}

				}
				break;
			case 66:
				{
				int LA76_6 = input.LA(2);
				if ( (synpred113_JPA2()) ) {
					alt76=1;
				}
				else if ( (synpred115_JPA2()) ) {
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
				if ( (synpred113_JPA2()) ) {
					alt76=1;
				}
				else if ( (synpred115_JPA2()) ) {
					alt76=2;
				}
				else if ( (true) ) {
					alt76=3;
				}

				}
				break;
			case 52:
				{
				int LA76_8 = input.LA(2);
				if ( (synpred113_JPA2()) ) {
					alt76=1;
				}
				else if ( (synpred115_JPA2()) ) {
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
				if ( (synpred113_JPA2()) ) {
					alt76=1;
				}
				else if ( (synpred115_JPA2()) ) {
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
				if ( (synpred113_JPA2()) ) {
					alt76=1;
				}
				else if ( (synpred115_JPA2()) ) {
					alt76=2;
				}
				else if ( (true) ) {
					alt76=3;
				}

				}
				break;
			case 93:
				{
				int LA76_18 = input.LA(2);
				if ( (synpred113_JPA2()) ) {
					alt76=1;
				}
				else if ( (synpred115_JPA2()) ) {
					alt76=2;
				}
				else if ( (true) ) {
					alt76=3;
				}

				}
				break;
			case 78:
				{
				int LA76_19 = input.LA(2);
				if ( (synpred113_JPA2()) ) {
					alt76=1;
				}
				else if ( (synpred115_JPA2()) ) {
					alt76=2;
				}
				else if ( (true) ) {
					alt76=3;
				}

				}
				break;
			case 79:
				{
				int LA76_20 = input.LA(2);
				if ( (synpred113_JPA2()) ) {
					alt76=1;
				}
				else if ( (synpred115_JPA2()) ) {
					alt76=2;
				}
				else if ( (true) ) {
					alt76=3;
				}

				}
				break;
			case 112:
				{
				int LA76_21 = input.LA(2);
				if ( (synpred113_JPA2()) ) {
					alt76=1;
				}
				else if ( (synpred115_JPA2()) ) {
					alt76=2;
				}
				else if ( (true) ) {
					alt76=3;
				}

				}
				break;
			case STRING_LITERAL:
			case 80:
			case 103:
			case 122:
			case 126:
			case 129:
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
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 76, 0, input);
				throw nvae;
			}
			switch (alt76) {
				case 1 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:286:7: arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_expression_in_between_expression2520);
					arithmetic_expression276=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression276.getTree());

					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:286:29: ( 'NOT' )?
					int alt73=2;
					int LA73_0 = input.LA(1);
					if ( (LA73_0==109) ) {
						alt73=1;
					}
					switch (alt73) {
						case 1 :
							// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:286:30: 'NOT'
							{
							string_literal277=(Token)match(input,109,FOLLOW_109_in_between_expression2523); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							string_literal277_tree = (Object)adaptor.create(string_literal277);
							adaptor.addChild(root_0, string_literal277_tree);
							}

							}
							break;

					}

					string_literal278=(Token)match(input,76,FOLLOW_76_in_between_expression2527); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal278_tree = (Object)adaptor.create(string_literal278);
					adaptor.addChild(root_0, string_literal278_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_between_expression2529);
					arithmetic_expression279=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression279.getTree());

					string_literal280=(Token)match(input,AND,FOLLOW_AND_in_between_expression2531); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal280_tree = (Object)adaptor.create(string_literal280);
					adaptor.addChild(root_0, string_literal280_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_between_expression2533);
					arithmetic_expression281=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression281.getTree());

					}
					break;
				case 2 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:287:7: string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_string_expression_in_between_expression2541);
					string_expression282=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression282.getTree());

					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:287:25: ( 'NOT' )?
					int alt74=2;
					int LA74_0 = input.LA(1);
					if ( (LA74_0==109) ) {
						alt74=1;
					}
					switch (alt74) {
						case 1 :
							// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:287:26: 'NOT'
							{
							string_literal283=(Token)match(input,109,FOLLOW_109_in_between_expression2544); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							string_literal283_tree = (Object)adaptor.create(string_literal283);
							adaptor.addChild(root_0, string_literal283_tree);
							}

							}
							break;

					}

					string_literal284=(Token)match(input,76,FOLLOW_76_in_between_expression2548); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal284_tree = (Object)adaptor.create(string_literal284);
					adaptor.addChild(root_0, string_literal284_tree);
					}

					pushFollow(FOLLOW_string_expression_in_between_expression2550);
					string_expression285=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression285.getTree());

					string_literal286=(Token)match(input,AND,FOLLOW_AND_in_between_expression2552); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal286_tree = (Object)adaptor.create(string_literal286);
					adaptor.addChild(root_0, string_literal286_tree);
					}

					pushFollow(FOLLOW_string_expression_in_between_expression2554);
					string_expression287=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression287.getTree());

					}
					break;
				case 3 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:288:7: datetime_expression ( 'NOT' )? 'BETWEEN' datetime_expression 'AND' datetime_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_datetime_expression_in_between_expression2562);
					datetime_expression288=datetime_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression288.getTree());

					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:288:27: ( 'NOT' )?
					int alt75=2;
					int LA75_0 = input.LA(1);
					if ( (LA75_0==109) ) {
						alt75=1;
					}
					switch (alt75) {
						case 1 :
							// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:288:28: 'NOT'
							{
							string_literal289=(Token)match(input,109,FOLLOW_109_in_between_expression2565); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							string_literal289_tree = (Object)adaptor.create(string_literal289);
							adaptor.addChild(root_0, string_literal289_tree);
							}

							}
							break;

					}

					string_literal290=(Token)match(input,76,FOLLOW_76_in_between_expression2569); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal290_tree = (Object)adaptor.create(string_literal290);
					adaptor.addChild(root_0, string_literal290_tree);
					}

					pushFollow(FOLLOW_datetime_expression_in_between_expression2571);
					datetime_expression291=datetime_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression291.getTree());

					string_literal292=(Token)match(input,AND,FOLLOW_AND_in_between_expression2573); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal292_tree = (Object)adaptor.create(string_literal292);
					adaptor.addChild(root_0, string_literal292_tree);
					}

					pushFollow(FOLLOW_datetime_expression_in_between_expression2575);
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:289:1: in_expression : ( path_expression | type_discriminator ) ( 'NOT' )? 'IN' ( '(' in_item ( ',' in_item )* ')' | subquery | collection_valued_input_parameter ) ;
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
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:290:5: ( ( path_expression | type_discriminator ) ( 'NOT' )? 'IN' ( '(' in_item ( ',' in_item )* ')' | subquery | collection_valued_input_parameter ) )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:290:7: ( path_expression | type_discriminator ) ( 'NOT' )? 'IN' ( '(' in_item ( ',' in_item )* ')' | subquery | collection_valued_input_parameter )
			{
			root_0 = (Object)adaptor.nil();


			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:290:7: ( path_expression | type_discriminator )
			int alt77=2;
			int LA77_0 = input.LA(1);
			if ( (LA77_0==WORD) ) {
				alt77=1;
			}
			else if ( (LA77_0==127) ) {
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
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:290:8: path_expression
					{
					pushFollow(FOLLOW_path_expression_in_in_expression2587);
					path_expression294=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression294.getTree());

					}
					break;
				case 2 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:290:26: type_discriminator
					{
					pushFollow(FOLLOW_type_discriminator_in_in_expression2591);
					type_discriminator295=type_discriminator();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, type_discriminator295.getTree());

					}
					break;

			}

			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:290:46: ( 'NOT' )?
			int alt78=2;
			int LA78_0 = input.LA(1);
			if ( (LA78_0==109) ) {
				alt78=1;
			}
			switch (alt78) {
				case 1 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:290:47: 'NOT'
					{
					string_literal296=(Token)match(input,109,FOLLOW_109_in_in_expression2595); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal296_tree = (Object)adaptor.create(string_literal296);
					adaptor.addChild(root_0, string_literal296_tree);
					}

					}
					break;

			}

			string_literal297=(Token)match(input,95,FOLLOW_95_in_in_expression2599); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal297_tree = (Object)adaptor.create(string_literal297);
			adaptor.addChild(root_0, string_literal297_tree);
			}

			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:291:13: ( '(' in_item ( ',' in_item )* ')' | subquery | collection_valued_input_parameter )
			int alt80=3;
			int LA80_0 = input.LA(1);
			if ( (LA80_0==LPAREN) ) {
				int LA80_1 = input.LA(2);
				if ( (LA80_1==117) ) {
					alt80=2;
				}
				else if ( (LA80_1==NAMED_PARAMETER||LA80_1==WORD||LA80_1==52||LA80_1==66) ) {
					alt80=1;
				}

				else {
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
			else if ( (LA80_0==NAMED_PARAMETER||LA80_0==52||LA80_0==66) ) {
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
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:291:15: '(' in_item ( ',' in_item )* ')'
					{
					char_literal298=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_in_expression2615); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal298_tree = (Object)adaptor.create(char_literal298);
					adaptor.addChild(root_0, char_literal298_tree);
					}

					pushFollow(FOLLOW_in_item_in_in_expression2617);
					in_item299=in_item();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, in_item299.getTree());

					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:291:27: ( ',' in_item )*
					loop79:
					while (true) {
						int alt79=2;
						int LA79_0 = input.LA(1);
						if ( (LA79_0==55) ) {
							alt79=1;
						}

						switch (alt79) {
						case 1 :
							// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:291:28: ',' in_item
							{
							char_literal300=(Token)match(input,55,FOLLOW_55_in_in_expression2620); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							char_literal300_tree = (Object)adaptor.create(char_literal300);
							adaptor.addChild(root_0, char_literal300_tree);
							}

							pushFollow(FOLLOW_in_item_in_in_expression2622);
							in_item301=in_item();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, in_item301.getTree());

							}
							break;

						default :
							break loop79;
						}
					}

					char_literal302=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_in_expression2626); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal302_tree = (Object)adaptor.create(char_literal302);
					adaptor.addChild(root_0, char_literal302_tree);
					}

					}
					break;
				case 2 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:292:15: subquery
					{
					pushFollow(FOLLOW_subquery_in_in_expression2642);
					subquery303=subquery();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, subquery303.getTree());

					}
					break;
				case 3 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:293:15: collection_valued_input_parameter
					{
					pushFollow(FOLLOW_collection_valued_input_parameter_in_in_expression2658);
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:294:1: in_item : ( literal | single_valued_input_parameter );
	public final JPA2Parser.in_item_return in_item() throws RecognitionException {
		JPA2Parser.in_item_return retval = new JPA2Parser.in_item_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope literal305 =null;
		ParserRuleReturnScope single_valued_input_parameter306 =null;


		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:295:5: ( literal | single_valued_input_parameter )
			int alt81=2;
			int LA81_0 = input.LA(1);
			if ( (LA81_0==WORD) ) {
				alt81=1;
			}
			else if ( (LA81_0==NAMED_PARAMETER||LA81_0==52||LA81_0==66) ) {
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
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:295:7: literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_literal_in_in_item2671);
					literal305=literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, literal305.getTree());

					}
					break;
				case 2 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:295:17: single_valued_input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_single_valued_input_parameter_in_in_item2675);
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:296:1: like_expression : string_expression ( 'NOT' )? 'LIKE' ( pattern_value | input_parameter ) ( 'ESCAPE' escape_character )? ;
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
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:297:5: ( string_expression ( 'NOT' )? 'LIKE' ( pattern_value | input_parameter ) ( 'ESCAPE' escape_character )? )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:297:7: string_expression ( 'NOT' )? 'LIKE' ( pattern_value | input_parameter ) ( 'ESCAPE' escape_character )?
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_string_expression_in_like_expression2686);
			string_expression307=string_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression307.getTree());

			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:297:25: ( 'NOT' )?
			int alt82=2;
			int LA82_0 = input.LA(1);
			if ( (LA82_0==109) ) {
				alt82=1;
			}
			switch (alt82) {
				case 1 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:297:26: 'NOT'
					{
					string_literal308=(Token)match(input,109,FOLLOW_109_in_like_expression2689); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal308_tree = (Object)adaptor.create(string_literal308);
					adaptor.addChild(root_0, string_literal308_tree);
					}

					}
					break;

			}

			string_literal309=(Token)match(input,101,FOLLOW_101_in_like_expression2693); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal309_tree = (Object)adaptor.create(string_literal309);
			adaptor.addChild(root_0, string_literal309_tree);
			}

			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:297:41: ( pattern_value | input_parameter )
			int alt83=2;
			int LA83_0 = input.LA(1);
			if ( (LA83_0==STRING_LITERAL) ) {
				alt83=1;
			}
			else if ( (LA83_0==NAMED_PARAMETER||LA83_0==52||LA83_0==66) ) {
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
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:297:42: pattern_value
					{
					pushFollow(FOLLOW_pattern_value_in_like_expression2696);
					pattern_value310=pattern_value();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, pattern_value310.getTree());

					}
					break;
				case 2 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:297:58: input_parameter
					{
					pushFollow(FOLLOW_input_parameter_in_like_expression2700);
					input_parameter311=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter311.getTree());

					}
					break;

			}

			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:297:74: ( 'ESCAPE' escape_character )?
			int alt84=2;
			int LA84_0 = input.LA(1);
			if ( (LA84_0==90) ) {
				alt84=1;
			}
			switch (alt84) {
				case 1 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:297:75: 'ESCAPE' escape_character
					{
					string_literal312=(Token)match(input,90,FOLLOW_90_in_like_expression2703); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal312_tree = (Object)adaptor.create(string_literal312);
					adaptor.addChild(root_0, string_literal312_tree);
					}

					pushFollow(FOLLOW_escape_character_in_like_expression2705);
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:298:1: null_comparison_expression : ( path_expression | input_parameter ) 'IS' ( 'NOT' )? 'NULL' ;
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
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:299:5: ( ( path_expression | input_parameter ) 'IS' ( 'NOT' )? 'NULL' )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:299:7: ( path_expression | input_parameter ) 'IS' ( 'NOT' )? 'NULL'
			{
			root_0 = (Object)adaptor.nil();


			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:299:7: ( path_expression | input_parameter )
			int alt85=2;
			int LA85_0 = input.LA(1);
			if ( (LA85_0==WORD) ) {
				alt85=1;
			}
			else if ( (LA85_0==NAMED_PARAMETER||LA85_0==52||LA85_0==66) ) {
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
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:299:8: path_expression
					{
					pushFollow(FOLLOW_path_expression_in_null_comparison_expression2719);
					path_expression314=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression314.getTree());

					}
					break;
				case 2 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:299:26: input_parameter
					{
					pushFollow(FOLLOW_input_parameter_in_null_comparison_expression2723);
					input_parameter315=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter315.getTree());

					}
					break;

			}

			string_literal316=(Token)match(input,97,FOLLOW_97_in_null_comparison_expression2726); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal316_tree = (Object)adaptor.create(string_literal316);
			adaptor.addChild(root_0, string_literal316_tree);
			}

			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:299:48: ( 'NOT' )?
			int alt86=2;
			int LA86_0 = input.LA(1);
			if ( (LA86_0==109) ) {
				alt86=1;
			}
			switch (alt86) {
				case 1 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:299:49: 'NOT'
					{
					string_literal317=(Token)match(input,109,FOLLOW_109_in_null_comparison_expression2729); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal317_tree = (Object)adaptor.create(string_literal317);
					adaptor.addChild(root_0, string_literal317_tree);
					}

					}
					break;

			}

			string_literal318=(Token)match(input,111,FOLLOW_111_in_null_comparison_expression2733); if (state.failed) return retval;
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:300:1: empty_collection_comparison_expression : path_expression 'IS' ( 'NOT' )? 'EMPTY' ;
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
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:301:5: ( path_expression 'IS' ( 'NOT' )? 'EMPTY' )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:301:7: path_expression 'IS' ( 'NOT' )? 'EMPTY'
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_path_expression_in_empty_collection_comparison_expression2744);
			path_expression319=path_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression319.getTree());

			string_literal320=(Token)match(input,97,FOLLOW_97_in_empty_collection_comparison_expression2746); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal320_tree = (Object)adaptor.create(string_literal320);
			adaptor.addChild(root_0, string_literal320_tree);
			}

			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:301:28: ( 'NOT' )?
			int alt87=2;
			int LA87_0 = input.LA(1);
			if ( (LA87_0==109) ) {
				alt87=1;
			}
			switch (alt87) {
				case 1 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:301:29: 'NOT'
					{
					string_literal321=(Token)match(input,109,FOLLOW_109_in_empty_collection_comparison_expression2749); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal321_tree = (Object)adaptor.create(string_literal321);
					adaptor.addChild(root_0, string_literal321_tree);
					}

					}
					break;

			}

			string_literal322=(Token)match(input,87,FOLLOW_87_in_empty_collection_comparison_expression2753); if (state.failed) return retval;
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:302:1: collection_member_expression : entity_or_value_expression ( 'NOT' )? 'MEMBER' ( 'OF' )? path_expression ;
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
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:303:5: ( entity_or_value_expression ( 'NOT' )? 'MEMBER' ( 'OF' )? path_expression )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:303:7: entity_or_value_expression ( 'NOT' )? 'MEMBER' ( 'OF' )? path_expression
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_entity_or_value_expression_in_collection_member_expression2764);
			entity_or_value_expression323=entity_or_value_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_or_value_expression323.getTree());

			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:303:35: ( 'NOT' )?
			int alt88=2;
			int LA88_0 = input.LA(1);
			if ( (LA88_0==109) ) {
				alt88=1;
			}
			switch (alt88) {
				case 1 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:303:36: 'NOT'
					{
					string_literal324=(Token)match(input,109,FOLLOW_109_in_collection_member_expression2768); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal324_tree = (Object)adaptor.create(string_literal324);
					adaptor.addChild(root_0, string_literal324_tree);
					}

					}
					break;

			}

			string_literal325=(Token)match(input,104,FOLLOW_104_in_collection_member_expression2772); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal325_tree = (Object)adaptor.create(string_literal325);
			adaptor.addChild(root_0, string_literal325_tree);
			}

			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:303:53: ( 'OF' )?
			int alt89=2;
			int LA89_0 = input.LA(1);
			if ( (LA89_0==114) ) {
				alt89=1;
			}
			switch (alt89) {
				case 1 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:303:54: 'OF'
					{
					string_literal326=(Token)match(input,114,FOLLOW_114_in_collection_member_expression2775); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal326_tree = (Object)adaptor.create(string_literal326);
					adaptor.addChild(root_0, string_literal326_tree);
					}

					}
					break;

			}

			pushFollow(FOLLOW_path_expression_in_collection_member_expression2779);
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:304:1: entity_or_value_expression : ( path_expression | simple_entity_or_value_expression );
	public final JPA2Parser.entity_or_value_expression_return entity_or_value_expression() throws RecognitionException {
		JPA2Parser.entity_or_value_expression_return retval = new JPA2Parser.entity_or_value_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression328 =null;
		ParserRuleReturnScope simple_entity_or_value_expression329 =null;


		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:305:5: ( path_expression | simple_entity_or_value_expression )
			int alt90=2;
			int LA90_0 = input.LA(1);
			if ( (LA90_0==WORD) ) {
				int LA90_1 = input.LA(2);
				if ( (LA90_1==57) ) {
					alt90=1;
				}
				else if ( (LA90_1==104||LA90_1==109) ) {
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
			else if ( (LA90_0==NAMED_PARAMETER||LA90_0==52||LA90_0==66) ) {
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
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:305:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_entity_or_value_expression2790);
					path_expression328=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression328.getTree());

					}
					break;
				case 2 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:306:7: simple_entity_or_value_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_simple_entity_or_value_expression_in_entity_or_value_expression2798);
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:307:1: simple_entity_or_value_expression : ( identification_variable | input_parameter | literal );
	public final JPA2Parser.simple_entity_or_value_expression_return simple_entity_or_value_expression() throws RecognitionException {
		JPA2Parser.simple_entity_or_value_expression_return retval = new JPA2Parser.simple_entity_or_value_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope identification_variable330 =null;
		ParserRuleReturnScope input_parameter331 =null;
		ParserRuleReturnScope literal332 =null;


		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:308:5: ( identification_variable | input_parameter | literal )
			int alt91=3;
			int LA91_0 = input.LA(1);
			if ( (LA91_0==WORD) ) {
				int LA91_1 = input.LA(2);
				if ( (synpred132_JPA2()) ) {
					alt91=1;
				}
				else if ( (true) ) {
					alt91=3;
				}

			}
			else if ( (LA91_0==NAMED_PARAMETER||LA91_0==52||LA91_0==66) ) {
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
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:308:7: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_simple_entity_or_value_expression2809);
					identification_variable330=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable330.getTree());

					}
					break;
				case 2 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:309:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_simple_entity_or_value_expression2817);
					input_parameter331=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter331.getTree());

					}
					break;
				case 3 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:310:7: literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_literal_in_simple_entity_or_value_expression2825);
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:311:1: exists_expression : ( 'NOT' )? 'EXISTS' subquery ;
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
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:312:5: ( ( 'NOT' )? 'EXISTS' subquery )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:312:7: ( 'NOT' )? 'EXISTS' subquery
			{
			root_0 = (Object)adaptor.nil();


			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:312:7: ( 'NOT' )?
			int alt92=2;
			int LA92_0 = input.LA(1);
			if ( (LA92_0==109) ) {
				alt92=1;
			}
			switch (alt92) {
				case 1 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:312:8: 'NOT'
					{
					string_literal333=(Token)match(input,109,FOLLOW_109_in_exists_expression2837); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal333_tree = (Object)adaptor.create(string_literal333);
					adaptor.addChild(root_0, string_literal333_tree);
					}

					}
					break;

			}

			string_literal334=(Token)match(input,91,FOLLOW_91_in_exists_expression2841); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal334_tree = (Object)adaptor.create(string_literal334);
			adaptor.addChild(root_0, string_literal334_tree);
			}

			pushFollow(FOLLOW_subquery_in_exists_expression2843);
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:313:1: all_or_any_expression : ( 'ALL' | 'ANY' | 'SOME' ) subquery ;
	public final JPA2Parser.all_or_any_expression_return all_or_any_expression() throws RecognitionException {
		JPA2Parser.all_or_any_expression_return retval = new JPA2Parser.all_or_any_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set336=null;
		ParserRuleReturnScope subquery337 =null;

		Object set336_tree=null;

		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:314:5: ( ( 'ALL' | 'ANY' | 'SOME' ) subquery )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:314:7: ( 'ALL' | 'ANY' | 'SOME' ) subquery
			{
			root_0 = (Object)adaptor.nil();


			set336=input.LT(1);
			if ( (input.LA(1) >= 73 && input.LA(1) <= 74)||input.LA(1)==120 ) {
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
			pushFollow(FOLLOW_subquery_in_all_or_any_expression2867);
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:315:1: comparison_expression : ( string_expression comparison_operator ( string_expression | all_or_any_expression ) | boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression ) | enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression ) | datetime_expression comparison_operator ( datetime_expression | all_or_any_expression ) | entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression ) | entity_type_expression ( '=' | '<>' ) entity_type_expression | arithmetic_expression comparison_operator ( arithmetic_expression | all_or_any_expression ) );
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
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:316:5: ( string_expression comparison_operator ( string_expression | all_or_any_expression ) | boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression ) | enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression ) | datetime_expression comparison_operator ( datetime_expression | all_or_any_expression ) | entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression ) | entity_type_expression ( '=' | '<>' ) entity_type_expression | arithmetic_expression comparison_operator ( arithmetic_expression | all_or_any_expression ) )
			int alt99=7;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA99_1 = input.LA(2);
				if ( (synpred138_JPA2()) ) {
					alt99=1;
				}
				else if ( (synpred141_JPA2()) ) {
					alt99=2;
				}
				else if ( (synpred144_JPA2()) ) {
					alt99=3;
				}
				else if ( (synpred146_JPA2()) ) {
					alt99=4;
				}
				else if ( (synpred149_JPA2()) ) {
					alt99=5;
				}
				else if ( (synpred151_JPA2()) ) {
					alt99=6;
				}
				else if ( (true) ) {
					alt99=7;
				}

				}
				break;
			case STRING_LITERAL:
			case 80:
			case 103:
			case 122:
			case 126:
			case 129:
				{
				alt99=1;
				}
				break;
			case 66:
				{
				int LA99_3 = input.LA(2);
				if ( (synpred138_JPA2()) ) {
					alt99=1;
				}
				else if ( (synpred141_JPA2()) ) {
					alt99=2;
				}
				else if ( (synpred144_JPA2()) ) {
					alt99=3;
				}
				else if ( (synpred146_JPA2()) ) {
					alt99=4;
				}
				else if ( (synpred149_JPA2()) ) {
					alt99=5;
				}
				else if ( (synpred151_JPA2()) ) {
					alt99=6;
				}
				else if ( (true) ) {
					alt99=7;
				}

				}
				break;
			case NAMED_PARAMETER:
				{
				int LA99_4 = input.LA(2);
				if ( (synpred138_JPA2()) ) {
					alt99=1;
				}
				else if ( (synpred141_JPA2()) ) {
					alt99=2;
				}
				else if ( (synpred144_JPA2()) ) {
					alt99=3;
				}
				else if ( (synpred146_JPA2()) ) {
					alt99=4;
				}
				else if ( (synpred149_JPA2()) ) {
					alt99=5;
				}
				else if ( (synpred151_JPA2()) ) {
					alt99=6;
				}
				else if ( (true) ) {
					alt99=7;
				}

				}
				break;
			case 52:
				{
				int LA99_5 = input.LA(2);
				if ( (synpred138_JPA2()) ) {
					alt99=1;
				}
				else if ( (synpred141_JPA2()) ) {
					alt99=2;
				}
				else if ( (synpred144_JPA2()) ) {
					alt99=3;
				}
				else if ( (synpred146_JPA2()) ) {
					alt99=4;
				}
				else if ( (synpred149_JPA2()) ) {
					alt99=5;
				}
				else if ( (synpred151_JPA2()) ) {
					alt99=6;
				}
				else if ( (true) ) {
					alt99=7;
				}

				}
				break;
			case COUNT:
				{
				int LA99_11 = input.LA(2);
				if ( (synpred138_JPA2()) ) {
					alt99=1;
				}
				else if ( (synpred146_JPA2()) ) {
					alt99=4;
				}
				else if ( (true) ) {
					alt99=7;
				}

				}
				break;
			case AVG:
			case MAX:
			case MIN:
			case SUM:
				{
				int LA99_12 = input.LA(2);
				if ( (synpred138_JPA2()) ) {
					alt99=1;
				}
				else if ( (synpred146_JPA2()) ) {
					alt99=4;
				}
				else if ( (true) ) {
					alt99=7;
				}

				}
				break;
			case 93:
				{
				int LA99_13 = input.LA(2);
				if ( (synpred138_JPA2()) ) {
					alt99=1;
				}
				else if ( (synpred141_JPA2()) ) {
					alt99=2;
				}
				else if ( (synpred146_JPA2()) ) {
					alt99=4;
				}
				else if ( (true) ) {
					alt99=7;
				}

				}
				break;
			case 78:
				{
				int LA99_14 = input.LA(2);
				if ( (synpred138_JPA2()) ) {
					alt99=1;
				}
				else if ( (synpred141_JPA2()) ) {
					alt99=2;
				}
				else if ( (synpred144_JPA2()) ) {
					alt99=3;
				}
				else if ( (synpred146_JPA2()) ) {
					alt99=4;
				}
				else if ( (true) ) {
					alt99=7;
				}

				}
				break;
			case 79:
				{
				int LA99_15 = input.LA(2);
				if ( (synpred138_JPA2()) ) {
					alt99=1;
				}
				else if ( (synpred141_JPA2()) ) {
					alt99=2;
				}
				else if ( (synpred144_JPA2()) ) {
					alt99=3;
				}
				else if ( (synpred146_JPA2()) ) {
					alt99=4;
				}
				else if ( (true) ) {
					alt99=7;
				}

				}
				break;
			case 112:
				{
				int LA99_16 = input.LA(2);
				if ( (synpred138_JPA2()) ) {
					alt99=1;
				}
				else if ( (synpred141_JPA2()) ) {
					alt99=2;
				}
				else if ( (synpred144_JPA2()) ) {
					alt99=3;
				}
				else if ( (synpred146_JPA2()) ) {
					alt99=4;
				}
				else if ( (true) ) {
					alt99=7;
				}

				}
				break;
			case LPAREN:
				{
				int LA99_17 = input.LA(2);
				if ( (synpred138_JPA2()) ) {
					alt99=1;
				}
				else if ( (synpred141_JPA2()) ) {
					alt99=2;
				}
				else if ( (synpred144_JPA2()) ) {
					alt99=3;
				}
				else if ( (synpred146_JPA2()) ) {
					alt99=4;
				}
				else if ( (true) ) {
					alt99=7;
				}

				}
				break;
			case 134:
			case 135:
				{
				alt99=2;
				}
				break;
			case 81:
			case 82:
			case 83:
				{
				alt99=4;
				}
				break;
			case 127:
				{
				alt99=6;
				}
				break;
			case INT_NUMERAL:
			case 54:
			case 56:
			case 59:
			case 72:
			case 96:
			case 100:
			case 102:
			case 106:
			case 119:
			case 121:
				{
				alt99=7;
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
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:316:7: string_expression comparison_operator ( string_expression | all_or_any_expression )
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_string_expression_in_comparison_expression2878);
					string_expression338=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression338.getTree());

					pushFollow(FOLLOW_comparison_operator_in_comparison_expression2880);
					comparison_operator339=comparison_operator();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_operator339.getTree());

					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:316:45: ( string_expression | all_or_any_expression )
					int alt93=2;
					int LA93_0 = input.LA(1);
					if ( (LA93_0==AVG||LA93_0==COUNT||(LA93_0 >= LPAREN && LA93_0 <= NAMED_PARAMETER)||(LA93_0 >= STRING_LITERAL && LA93_0 <= SUM)||LA93_0==WORD||LA93_0==52||LA93_0==66||(LA93_0 >= 78 && LA93_0 <= 80)||LA93_0==93||LA93_0==103||LA93_0==112||LA93_0==122||LA93_0==126||LA93_0==129) ) {
						alt93=1;
					}
					else if ( ((LA93_0 >= 73 && LA93_0 <= 74)||LA93_0==120) ) {
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
							// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:316:46: string_expression
							{
							pushFollow(FOLLOW_string_expression_in_comparison_expression2883);
							string_expression340=string_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression340.getTree());

							}
							break;
						case 2 :
							// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:316:66: all_or_any_expression
							{
							pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression2887);
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
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:317:7: boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression )
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_boolean_expression_in_comparison_expression2896);
					boolean_expression342=boolean_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_expression342.getTree());

					set343=input.LT(1);
					if ( (input.LA(1) >= 62 && input.LA(1) <= 63) ) {
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
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:317:39: ( boolean_expression | all_or_any_expression )
					int alt94=2;
					int LA94_0 = input.LA(1);
					if ( (LA94_0==LPAREN||LA94_0==NAMED_PARAMETER||LA94_0==WORD||LA94_0==52||LA94_0==66||(LA94_0 >= 78 && LA94_0 <= 79)||LA94_0==93||LA94_0==112||(LA94_0 >= 134 && LA94_0 <= 135)) ) {
						alt94=1;
					}
					else if ( ((LA94_0 >= 73 && LA94_0 <= 74)||LA94_0==120) ) {
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
							// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:317:40: boolean_expression
							{
							pushFollow(FOLLOW_boolean_expression_in_comparison_expression2907);
							boolean_expression344=boolean_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_expression344.getTree());

							}
							break;
						case 2 :
							// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:317:61: all_or_any_expression
							{
							pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression2911);
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
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:318:7: enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression )
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_enum_expression_in_comparison_expression2920);
					enum_expression346=enum_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_expression346.getTree());

					set347=input.LT(1);
					if ( (input.LA(1) >= 62 && input.LA(1) <= 63) ) {
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
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:318:34: ( enum_expression | all_or_any_expression )
					int alt95=2;
					int LA95_0 = input.LA(1);
					if ( (LA95_0==LPAREN||LA95_0==NAMED_PARAMETER||LA95_0==WORD||LA95_0==52||LA95_0==66||(LA95_0 >= 78 && LA95_0 <= 79)||LA95_0==112) ) {
						alt95=1;
					}
					else if ( ((LA95_0 >= 73 && LA95_0 <= 74)||LA95_0==120) ) {
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
							// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:318:35: enum_expression
							{
							pushFollow(FOLLOW_enum_expression_in_comparison_expression2929);
							enum_expression348=enum_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_expression348.getTree());

							}
							break;
						case 2 :
							// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:318:53: all_or_any_expression
							{
							pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression2933);
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
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:319:7: datetime_expression comparison_operator ( datetime_expression | all_or_any_expression )
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_datetime_expression_in_comparison_expression2942);
					datetime_expression350=datetime_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression350.getTree());

					pushFollow(FOLLOW_comparison_operator_in_comparison_expression2944);
					comparison_operator351=comparison_operator();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_operator351.getTree());

					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:319:47: ( datetime_expression | all_or_any_expression )
					int alt96=2;
					int LA96_0 = input.LA(1);
					if ( (LA96_0==AVG||LA96_0==COUNT||(LA96_0 >= LPAREN && LA96_0 <= NAMED_PARAMETER)||LA96_0==SUM||LA96_0==WORD||LA96_0==52||LA96_0==66||(LA96_0 >= 78 && LA96_0 <= 79)||(LA96_0 >= 81 && LA96_0 <= 83)||LA96_0==93||LA96_0==112) ) {
						alt96=1;
					}
					else if ( ((LA96_0 >= 73 && LA96_0 <= 74)||LA96_0==120) ) {
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
							// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:319:48: datetime_expression
							{
							pushFollow(FOLLOW_datetime_expression_in_comparison_expression2947);
							datetime_expression352=datetime_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, datetime_expression352.getTree());

							}
							break;
						case 2 :
							// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:319:70: all_or_any_expression
							{
							pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression2951);
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
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:320:7: entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression )
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_entity_expression_in_comparison_expression2960);
					entity_expression354=entity_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_expression354.getTree());

					set355=input.LT(1);
					if ( (input.LA(1) >= 62 && input.LA(1) <= 63) ) {
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
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:320:38: ( entity_expression | all_or_any_expression )
					int alt97=2;
					int LA97_0 = input.LA(1);
					if ( (LA97_0==NAMED_PARAMETER||LA97_0==WORD||LA97_0==52||LA97_0==66) ) {
						alt97=1;
					}
					else if ( ((LA97_0 >= 73 && LA97_0 <= 74)||LA97_0==120) ) {
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
							// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:320:39: entity_expression
							{
							pushFollow(FOLLOW_entity_expression_in_comparison_expression2971);
							entity_expression356=entity_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_expression356.getTree());

							}
							break;
						case 2 :
							// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:320:59: all_or_any_expression
							{
							pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression2975);
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
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:321:7: entity_type_expression ( '=' | '<>' ) entity_type_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_entity_type_expression_in_comparison_expression2984);
					entity_type_expression358=entity_type_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_type_expression358.getTree());

					set359=input.LT(1);
					if ( (input.LA(1) >= 62 && input.LA(1) <= 63) ) {
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
					pushFollow(FOLLOW_entity_type_expression_in_comparison_expression2994);
					entity_type_expression360=entity_type_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_type_expression360.getTree());

					}
					break;
				case 7 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:322:7: arithmetic_expression comparison_operator ( arithmetic_expression | all_or_any_expression )
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_expression_in_comparison_expression3002);
					arithmetic_expression361=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression361.getTree());

					pushFollow(FOLLOW_comparison_operator_in_comparison_expression3004);
					comparison_operator362=comparison_operator();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, comparison_operator362.getTree());

					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:322:49: ( arithmetic_expression | all_or_any_expression )
					int alt98=2;
					int LA98_0 = input.LA(1);
					if ( (LA98_0==AVG||LA98_0==COUNT||LA98_0==INT_NUMERAL||(LA98_0 >= LPAREN && LA98_0 <= NAMED_PARAMETER)||LA98_0==SUM||LA98_0==WORD||LA98_0==52||LA98_0==54||LA98_0==56||LA98_0==59||LA98_0==66||LA98_0==72||(LA98_0 >= 78 && LA98_0 <= 79)||LA98_0==93||LA98_0==96||LA98_0==100||LA98_0==102||LA98_0==106||LA98_0==112||LA98_0==119||LA98_0==121) ) {
						alt98=1;
					}
					else if ( ((LA98_0 >= 73 && LA98_0 <= 74)||LA98_0==120) ) {
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
							// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:322:50: arithmetic_expression
							{
							pushFollow(FOLLOW_arithmetic_expression_in_comparison_expression3007);
							arithmetic_expression363=arithmetic_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression363.getTree());

							}
							break;
						case 2 :
							// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:322:74: all_or_any_expression
							{
							pushFollow(FOLLOW_all_or_any_expression_in_comparison_expression3011);
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:324:1: comparison_operator : ( '=' | '>' | '>=' | '<' | '<=' | '<>' );
	public final JPA2Parser.comparison_operator_return comparison_operator() throws RecognitionException {
		JPA2Parser.comparison_operator_return retval = new JPA2Parser.comparison_operator_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set365=null;

		Object set365_tree=null;

		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:325:5: ( '=' | '>' | '>=' | '<' | '<=' | '<>' )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set365=input.LT(1);
			if ( (input.LA(1) >= 60 && input.LA(1) <= 65) ) {
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:331:1: arithmetic_expression : ( arithmetic_term | arithmetic_term ( '+' | '-' ) arithmetic_term );
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
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:332:5: ( arithmetic_term | arithmetic_term ( '+' | '-' ) arithmetic_term )
			int alt100=2;
			switch ( input.LA(1) ) {
			case 54:
			case 56:
				{
				int LA100_1 = input.LA(2);
				if ( (synpred158_JPA2()) ) {
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
				if ( (synpred158_JPA2()) ) {
					alt100=1;
				}
				else if ( (true) ) {
					alt100=2;
				}

				}
				break;
			case 59:
				{
				int LA100_3 = input.LA(2);
				if ( (synpred158_JPA2()) ) {
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
				if ( (synpred158_JPA2()) ) {
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
				if ( (synpred158_JPA2()) ) {
					alt100=1;
				}
				else if ( (true) ) {
					alt100=2;
				}

				}
				break;
			case 66:
				{
				int LA100_6 = input.LA(2);
				if ( (synpred158_JPA2()) ) {
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
				if ( (synpred158_JPA2()) ) {
					alt100=1;
				}
				else if ( (true) ) {
					alt100=2;
				}

				}
				break;
			case 52:
				{
				int LA100_8 = input.LA(2);
				if ( (synpred158_JPA2()) ) {
					alt100=1;
				}
				else if ( (true) ) {
					alt100=2;
				}

				}
				break;
			case 100:
				{
				int LA100_9 = input.LA(2);
				if ( (synpred158_JPA2()) ) {
					alt100=1;
				}
				else if ( (true) ) {
					alt100=2;
				}

				}
				break;
			case 102:
				{
				int LA100_10 = input.LA(2);
				if ( (synpred158_JPA2()) ) {
					alt100=1;
				}
				else if ( (true) ) {
					alt100=2;
				}

				}
				break;
			case 72:
				{
				int LA100_11 = input.LA(2);
				if ( (synpred158_JPA2()) ) {
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
				if ( (synpred158_JPA2()) ) {
					alt100=1;
				}
				else if ( (true) ) {
					alt100=2;
				}

				}
				break;
			case 106:
				{
				int LA100_13 = input.LA(2);
				if ( (synpred158_JPA2()) ) {
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
				if ( (synpred158_JPA2()) ) {
					alt100=1;
				}
				else if ( (true) ) {
					alt100=2;
				}

				}
				break;
			case 96:
				{
				int LA100_15 = input.LA(2);
				if ( (synpred158_JPA2()) ) {
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
				if ( (synpred158_JPA2()) ) {
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
				if ( (synpred158_JPA2()) ) {
					alt100=1;
				}
				else if ( (true) ) {
					alt100=2;
				}

				}
				break;
			case 93:
				{
				int LA100_18 = input.LA(2);
				if ( (synpred158_JPA2()) ) {
					alt100=1;
				}
				else if ( (true) ) {
					alt100=2;
				}

				}
				break;
			case 78:
				{
				int LA100_19 = input.LA(2);
				if ( (synpred158_JPA2()) ) {
					alt100=1;
				}
				else if ( (true) ) {
					alt100=2;
				}

				}
				break;
			case 79:
				{
				int LA100_20 = input.LA(2);
				if ( (synpred158_JPA2()) ) {
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
				if ( (synpred158_JPA2()) ) {
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
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:332:7: arithmetic_term
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_term_in_arithmetic_expression3075);
					arithmetic_term366=arithmetic_term();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_term366.getTree());

					}
					break;
				case 2 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:333:7: arithmetic_term ( '+' | '-' ) arithmetic_term
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_term_in_arithmetic_expression3083);
					arithmetic_term367=arithmetic_term();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_term367.getTree());

					set368=input.LT(1);
					if ( input.LA(1)==54||input.LA(1)==56 ) {
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
					pushFollow(FOLLOW_arithmetic_term_in_arithmetic_expression3093);
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:334:1: arithmetic_term : ( arithmetic_factor | arithmetic_factor ( '*' | '/' ) arithmetic_factor );
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
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:335:5: ( arithmetic_factor | arithmetic_factor ( '*' | '/' ) arithmetic_factor )
			int alt101=2;
			switch ( input.LA(1) ) {
			case 54:
			case 56:
				{
				int LA101_1 = input.LA(2);
				if ( (synpred160_JPA2()) ) {
					alt101=1;
				}
				else if ( (true) ) {
					alt101=2;
				}

				}
				break;
			case WORD:
				{
				int LA101_2 = input.LA(2);
				if ( (synpred160_JPA2()) ) {
					alt101=1;
				}
				else if ( (true) ) {
					alt101=2;
				}

				}
				break;
			case 59:
				{
				int LA101_3 = input.LA(2);
				if ( (synpred160_JPA2()) ) {
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
				if ( (synpred160_JPA2()) ) {
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
				if ( (synpred160_JPA2()) ) {
					alt101=1;
				}
				else if ( (true) ) {
					alt101=2;
				}

				}
				break;
			case 66:
				{
				int LA101_6 = input.LA(2);
				if ( (synpred160_JPA2()) ) {
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
				if ( (synpred160_JPA2()) ) {
					alt101=1;
				}
				else if ( (true) ) {
					alt101=2;
				}

				}
				break;
			case 52:
				{
				int LA101_8 = input.LA(2);
				if ( (synpred160_JPA2()) ) {
					alt101=1;
				}
				else if ( (true) ) {
					alt101=2;
				}

				}
				break;
			case 100:
				{
				int LA101_9 = input.LA(2);
				if ( (synpred160_JPA2()) ) {
					alt101=1;
				}
				else if ( (true) ) {
					alt101=2;
				}

				}
				break;
			case 102:
				{
				int LA101_10 = input.LA(2);
				if ( (synpred160_JPA2()) ) {
					alt101=1;
				}
				else if ( (true) ) {
					alt101=2;
				}

				}
				break;
			case 72:
				{
				int LA101_11 = input.LA(2);
				if ( (synpred160_JPA2()) ) {
					alt101=1;
				}
				else if ( (true) ) {
					alt101=2;
				}

				}
				break;
			case 121:
				{
				int LA101_12 = input.LA(2);
				if ( (synpred160_JPA2()) ) {
					alt101=1;
				}
				else if ( (true) ) {
					alt101=2;
				}

				}
				break;
			case 106:
				{
				int LA101_13 = input.LA(2);
				if ( (synpred160_JPA2()) ) {
					alt101=1;
				}
				else if ( (true) ) {
					alt101=2;
				}

				}
				break;
			case 119:
				{
				int LA101_14 = input.LA(2);
				if ( (synpred160_JPA2()) ) {
					alt101=1;
				}
				else if ( (true) ) {
					alt101=2;
				}

				}
				break;
			case 96:
				{
				int LA101_15 = input.LA(2);
				if ( (synpred160_JPA2()) ) {
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
				if ( (synpred160_JPA2()) ) {
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
				if ( (synpred160_JPA2()) ) {
					alt101=1;
				}
				else if ( (true) ) {
					alt101=2;
				}

				}
				break;
			case 93:
				{
				int LA101_18 = input.LA(2);
				if ( (synpred160_JPA2()) ) {
					alt101=1;
				}
				else if ( (true) ) {
					alt101=2;
				}

				}
				break;
			case 78:
				{
				int LA101_19 = input.LA(2);
				if ( (synpred160_JPA2()) ) {
					alt101=1;
				}
				else if ( (true) ) {
					alt101=2;
				}

				}
				break;
			case 79:
				{
				int LA101_20 = input.LA(2);
				if ( (synpred160_JPA2()) ) {
					alt101=1;
				}
				else if ( (true) ) {
					alt101=2;
				}

				}
				break;
			case 112:
				{
				int LA101_21 = input.LA(2);
				if ( (synpred160_JPA2()) ) {
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
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:335:7: arithmetic_factor
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_factor_in_arithmetic_term3104);
					arithmetic_factor370=arithmetic_factor();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_factor370.getTree());

					}
					break;
				case 2 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:336:7: arithmetic_factor ( '*' | '/' ) arithmetic_factor
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_arithmetic_factor_in_arithmetic_term3112);
					arithmetic_factor371=arithmetic_factor();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_factor371.getTree());

					set372=input.LT(1);
					if ( input.LA(1)==53||input.LA(1)==58 ) {
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
					pushFollow(FOLLOW_arithmetic_factor_in_arithmetic_term3123);
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:337:1: arithmetic_factor : ( ( '+' | '-' ) )? arithmetic_primary ;
	public final JPA2Parser.arithmetic_factor_return arithmetic_factor() throws RecognitionException {
		JPA2Parser.arithmetic_factor_return retval = new JPA2Parser.arithmetic_factor_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set374=null;
		ParserRuleReturnScope arithmetic_primary375 =null;

		Object set374_tree=null;

		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:338:5: ( ( ( '+' | '-' ) )? arithmetic_primary )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:338:7: ( ( '+' | '-' ) )? arithmetic_primary
			{
			root_0 = (Object)adaptor.nil();


			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:338:7: ( ( '+' | '-' ) )?
			int alt102=2;
			int LA102_0 = input.LA(1);
			if ( (LA102_0==54||LA102_0==56) ) {
				alt102=1;
			}
			switch (alt102) {
				case 1 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:
					{
					set374=input.LT(1);
					if ( input.LA(1)==54||input.LA(1)==56 ) {
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

			pushFollow(FOLLOW_arithmetic_primary_in_arithmetic_factor3146);
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:339:1: arithmetic_primary : ( path_expression | numeric_literal | '(' arithmetic_expression ')' | input_parameter | functions_returning_numerics | aggregate_expression | case_expression | function_invocation | subquery );
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
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:340:5: ( path_expression | numeric_literal | '(' arithmetic_expression ')' | input_parameter | functions_returning_numerics | aggregate_expression | case_expression | function_invocation | subquery )
			int alt103=9;
			switch ( input.LA(1) ) {
			case WORD:
				{
				alt103=1;
				}
				break;
			case INT_NUMERAL:
			case 59:
				{
				alt103=2;
				}
				break;
			case LPAREN:
				{
				int LA103_4 = input.LA(2);
				if ( (synpred166_JPA2()) ) {
					alt103=3;
				}
				else if ( (true) ) {
					alt103=9;
				}

				}
				break;
			case NAMED_PARAMETER:
			case 52:
			case 66:
				{
				alt103=4;
				}
				break;
			case 72:
			case 96:
			case 100:
			case 102:
			case 106:
			case 119:
			case 121:
				{
				alt103=5;
				}
				break;
			case AVG:
			case COUNT:
			case MAX:
			case MIN:
			case SUM:
				{
				alt103=6;
				}
				break;
			case 93:
				{
				int LA103_17 = input.LA(2);
				if ( (synpred169_JPA2()) ) {
					alt103=6;
				}
				else if ( (synpred171_JPA2()) ) {
					alt103=8;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 103, 17, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 78:
			case 79:
			case 112:
				{
				alt103=7;
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
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:340:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_arithmetic_primary3157);
					path_expression376=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression376.getTree());

					}
					break;
				case 2 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:341:7: numeric_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_numeric_literal_in_arithmetic_primary3165);
					numeric_literal377=numeric_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, numeric_literal377.getTree());

					}
					break;
				case 3 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:342:7: '(' arithmetic_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					char_literal378=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_arithmetic_primary3173); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal378_tree = (Object)adaptor.create(char_literal378);
					adaptor.addChild(root_0, char_literal378_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_arithmetic_primary3174);
					arithmetic_expression379=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression379.getTree());

					char_literal380=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_arithmetic_primary3175); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal380_tree = (Object)adaptor.create(char_literal380);
					adaptor.addChild(root_0, char_literal380_tree);
					}

					}
					break;
				case 4 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:343:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_arithmetic_primary3183);
					input_parameter381=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter381.getTree());

					}
					break;
				case 5 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:344:7: functions_returning_numerics
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_functions_returning_numerics_in_arithmetic_primary3191);
					functions_returning_numerics382=functions_returning_numerics();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, functions_returning_numerics382.getTree());

					}
					break;
				case 6 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:345:7: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_arithmetic_primary3199);
					aggregate_expression383=aggregate_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression383.getTree());

					}
					break;
				case 7 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:346:7: case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_case_expression_in_arithmetic_primary3207);
					case_expression384=case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, case_expression384.getTree());

					}
					break;
				case 8 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:347:7: function_invocation
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_function_invocation_in_arithmetic_primary3215);
					function_invocation385=function_invocation();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_invocation385.getTree());

					}
					break;
				case 9 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:348:7: subquery
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_subquery_in_arithmetic_primary3223);
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:349:1: string_expression : ( path_expression | string_literal | input_parameter | functions_returning_strings | aggregate_expression | case_expression | function_invocation | subquery );
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
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:350:5: ( path_expression | string_literal | input_parameter | functions_returning_strings | aggregate_expression | case_expression | function_invocation | subquery )
			int alt104=8;
			switch ( input.LA(1) ) {
			case WORD:
				{
				alt104=1;
				}
				break;
			case STRING_LITERAL:
				{
				alt104=2;
				}
				break;
			case NAMED_PARAMETER:
			case 52:
			case 66:
				{
				alt104=3;
				}
				break;
			case 80:
			case 103:
			case 122:
			case 126:
			case 129:
				{
				alt104=4;
				}
				break;
			case AVG:
			case COUNT:
			case MAX:
			case MIN:
			case SUM:
				{
				alt104=5;
				}
				break;
			case 93:
				{
				int LA104_13 = input.LA(2);
				if ( (synpred176_JPA2()) ) {
					alt104=5;
				}
				else if ( (synpred178_JPA2()) ) {
					alt104=7;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 104, 13, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 78:
			case 79:
			case 112:
				{
				alt104=6;
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
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:350:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_string_expression3234);
					path_expression387=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression387.getTree());

					}
					break;
				case 2 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:351:7: string_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_string_literal_in_string_expression3242);
					string_literal388=string_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_literal388.getTree());

					}
					break;
				case 3 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:352:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_string_expression3250);
					input_parameter389=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter389.getTree());

					}
					break;
				case 4 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:353:7: functions_returning_strings
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_functions_returning_strings_in_string_expression3258);
					functions_returning_strings390=functions_returning_strings();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, functions_returning_strings390.getTree());

					}
					break;
				case 5 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:354:7: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_string_expression3266);
					aggregate_expression391=aggregate_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression391.getTree());

					}
					break;
				case 6 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:355:7: case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_case_expression_in_string_expression3274);
					case_expression392=case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, case_expression392.getTree());

					}
					break;
				case 7 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:356:7: function_invocation
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_function_invocation_in_string_expression3282);
					function_invocation393=function_invocation();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_invocation393.getTree());

					}
					break;
				case 8 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:357:7: subquery
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_subquery_in_string_expression3290);
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:358:1: datetime_expression : ( path_expression | input_parameter | functions_returning_datetime | aggregate_expression | case_expression | function_invocation | date_time_timestamp_literal | subquery );
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
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:359:5: ( path_expression | input_parameter | functions_returning_datetime | aggregate_expression | case_expression | function_invocation | date_time_timestamp_literal | subquery )
			int alt105=8;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA105_1 = input.LA(2);
				if ( (synpred179_JPA2()) ) {
					alt105=1;
				}
				else if ( (synpred185_JPA2()) ) {
					alt105=7;
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
			case 52:
			case 66:
				{
				alt105=2;
				}
				break;
			case 81:
			case 82:
			case 83:
				{
				alt105=3;
				}
				break;
			case AVG:
			case COUNT:
			case MAX:
			case MIN:
			case SUM:
				{
				alt105=4;
				}
				break;
			case 93:
				{
				int LA105_8 = input.LA(2);
				if ( (synpred182_JPA2()) ) {
					alt105=4;
				}
				else if ( (synpred184_JPA2()) ) {
					alt105=6;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 105, 8, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 78:
			case 79:
			case 112:
				{
				alt105=5;
				}
				break;
			case LPAREN:
				{
				alt105=8;
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
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:359:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_datetime_expression3301);
					path_expression395=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression395.getTree());

					}
					break;
				case 2 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:360:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_datetime_expression3309);
					input_parameter396=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter396.getTree());

					}
					break;
				case 3 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:361:7: functions_returning_datetime
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_functions_returning_datetime_in_datetime_expression3317);
					functions_returning_datetime397=functions_returning_datetime();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, functions_returning_datetime397.getTree());

					}
					break;
				case 4 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:362:7: aggregate_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_aggregate_expression_in_datetime_expression3325);
					aggregate_expression398=aggregate_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, aggregate_expression398.getTree());

					}
					break;
				case 5 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:363:7: case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_case_expression_in_datetime_expression3333);
					case_expression399=case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, case_expression399.getTree());

					}
					break;
				case 6 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:364:7: function_invocation
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_function_invocation_in_datetime_expression3341);
					function_invocation400=function_invocation();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_invocation400.getTree());

					}
					break;
				case 7 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:365:7: date_time_timestamp_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_date_time_timestamp_literal_in_datetime_expression3349);
					date_time_timestamp_literal401=date_time_timestamp_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, date_time_timestamp_literal401.getTree());

					}
					break;
				case 8 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:366:7: subquery
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_subquery_in_datetime_expression3357);
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:367:1: boolean_expression : ( path_expression | boolean_literal | input_parameter | case_expression | function_invocation | subquery );
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
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:368:5: ( path_expression | boolean_literal | input_parameter | case_expression | function_invocation | subquery )
			int alt106=6;
			switch ( input.LA(1) ) {
			case WORD:
				{
				alt106=1;
				}
				break;
			case 134:
			case 135:
				{
				alt106=2;
				}
				break;
			case NAMED_PARAMETER:
			case 52:
			case 66:
				{
				alt106=3;
				}
				break;
			case 78:
			case 79:
			case 112:
				{
				alt106=4;
				}
				break;
			case 93:
				{
				alt106=5;
				}
				break;
			case LPAREN:
				{
				alt106=6;
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
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:368:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_boolean_expression3368);
					path_expression403=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression403.getTree());

					}
					break;
				case 2 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:369:7: boolean_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_boolean_literal_in_boolean_expression3376);
					boolean_literal404=boolean_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, boolean_literal404.getTree());

					}
					break;
				case 3 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:370:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_boolean_expression3384);
					input_parameter405=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter405.getTree());

					}
					break;
				case 4 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:371:7: case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_case_expression_in_boolean_expression3392);
					case_expression406=case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, case_expression406.getTree());

					}
					break;
				case 5 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:372:7: function_invocation
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_function_invocation_in_boolean_expression3400);
					function_invocation407=function_invocation();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_invocation407.getTree());

					}
					break;
				case 6 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:373:7: subquery
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_subquery_in_boolean_expression3408);
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:374:1: enum_expression : ( path_expression | enum_literal | input_parameter | case_expression | subquery );
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
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:375:5: ( path_expression | enum_literal | input_parameter | case_expression | subquery )
			int alt107=5;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA107_1 = input.LA(2);
				if ( (LA107_1==57) ) {
					alt107=1;
				}
				else if ( (LA107_1==EOF||LA107_1==AND||(LA107_1 >= GROUP && LA107_1 <= INNER)||(LA107_1 >= JOIN && LA107_1 <= LEFT)||(LA107_1 >= OR && LA107_1 <= ORDER)||LA107_1==RPAREN||LA107_1==WORD||LA107_1==55||(LA107_1 >= 62 && LA107_1 <= 63)||LA107_1==75||LA107_1==86||LA107_1==88||LA107_1==92||LA107_1==123||(LA107_1 >= 131 && LA107_1 <= 132)) ) {
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
				break;
			case NAMED_PARAMETER:
			case 52:
			case 66:
				{
				alt107=3;
				}
				break;
			case 78:
			case 79:
			case 112:
				{
				alt107=4;
				}
				break;
			case LPAREN:
				{
				alt107=5;
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
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:375:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_enum_expression3419);
					path_expression409=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression409.getTree());

					}
					break;
				case 2 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:376:7: enum_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_enum_literal_in_enum_expression3427);
					enum_literal410=enum_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_literal410.getTree());

					}
					break;
				case 3 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:377:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_enum_expression3435);
					input_parameter411=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter411.getTree());

					}
					break;
				case 4 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:378:7: case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_case_expression_in_enum_expression3443);
					case_expression412=case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, case_expression412.getTree());

					}
					break;
				case 5 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:379:7: subquery
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_subquery_in_enum_expression3451);
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:380:1: entity_expression : ( path_expression | simple_entity_expression );
	public final JPA2Parser.entity_expression_return entity_expression() throws RecognitionException {
		JPA2Parser.entity_expression_return retval = new JPA2Parser.entity_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression414 =null;
		ParserRuleReturnScope simple_entity_expression415 =null;


		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:381:5: ( path_expression | simple_entity_expression )
			int alt108=2;
			int LA108_0 = input.LA(1);
			if ( (LA108_0==WORD) ) {
				int LA108_1 = input.LA(2);
				if ( (LA108_1==57) ) {
					alt108=1;
				}
				else if ( (LA108_1==EOF||LA108_1==AND||(LA108_1 >= GROUP && LA108_1 <= INNER)||(LA108_1 >= JOIN && LA108_1 <= LEFT)||(LA108_1 >= OR && LA108_1 <= ORDER)||LA108_1==RPAREN||LA108_1==55||(LA108_1 >= 62 && LA108_1 <= 63)||LA108_1==123||LA108_1==132) ) {
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
			else if ( (LA108_0==NAMED_PARAMETER||LA108_0==52||LA108_0==66) ) {
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
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:381:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_entity_expression3462);
					path_expression414=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression414.getTree());

					}
					break;
				case 2 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:382:7: simple_entity_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_simple_entity_expression_in_entity_expression3470);
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:383:1: simple_entity_expression : ( identification_variable | input_parameter );
	public final JPA2Parser.simple_entity_expression_return simple_entity_expression() throws RecognitionException {
		JPA2Parser.simple_entity_expression_return retval = new JPA2Parser.simple_entity_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope identification_variable416 =null;
		ParserRuleReturnScope input_parameter417 =null;


		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:384:5: ( identification_variable | input_parameter )
			int alt109=2;
			int LA109_0 = input.LA(1);
			if ( (LA109_0==WORD) ) {
				alt109=1;
			}
			else if ( (LA109_0==NAMED_PARAMETER||LA109_0==52||LA109_0==66) ) {
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
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:384:7: identification_variable
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_identification_variable_in_simple_entity_expression3481);
					identification_variable416=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable416.getTree());

					}
					break;
				case 2 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:385:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_simple_entity_expression3489);
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:386:1: entity_type_expression : ( type_discriminator | entity_type_literal | input_parameter );
	public final JPA2Parser.entity_type_expression_return entity_type_expression() throws RecognitionException {
		JPA2Parser.entity_type_expression_return retval = new JPA2Parser.entity_type_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope type_discriminator418 =null;
		ParserRuleReturnScope entity_type_literal419 =null;
		ParserRuleReturnScope input_parameter420 =null;


		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:387:5: ( type_discriminator | entity_type_literal | input_parameter )
			int alt110=3;
			switch ( input.LA(1) ) {
			case 127:
				{
				alt110=1;
				}
				break;
			case WORD:
				{
				alt110=2;
				}
				break;
			case NAMED_PARAMETER:
			case 52:
			case 66:
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
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:387:7: type_discriminator
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_type_discriminator_in_entity_type_expression3500);
					type_discriminator418=type_discriminator();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, type_discriminator418.getTree());

					}
					break;
				case 2 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:388:7: entity_type_literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_entity_type_literal_in_entity_type_expression3508);
					entity_type_literal419=entity_type_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, entity_type_literal419.getTree());

					}
					break;
				case 3 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:389:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_entity_type_expression3516);
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:390:1: type_discriminator : 'TYPE' ( general_identification_variable | path_expression | input_parameter ) ;
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
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:391:5: ( 'TYPE' ( general_identification_variable | path_expression | input_parameter ) )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:391:7: 'TYPE' ( general_identification_variable | path_expression | input_parameter )
			{
			root_0 = (Object)adaptor.nil();


			string_literal421=(Token)match(input,127,FOLLOW_127_in_type_discriminator3527); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal421_tree = (Object)adaptor.create(string_literal421);
			adaptor.addChild(root_0, string_literal421_tree);
			}

			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:391:13: ( general_identification_variable | path_expression | input_parameter )
			int alt111=3;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA111_1 = input.LA(2);
				if ( (LA111_1==EOF||LA111_1==AND||(LA111_1 >= GROUP && LA111_1 <= INNER)||(LA111_1 >= JOIN && LA111_1 <= LEFT)||(LA111_1 >= OR && LA111_1 <= ORDER)||LA111_1==RPAREN||LA111_1==WORD||LA111_1==55||(LA111_1 >= 62 && LA111_1 <= 63)||LA111_1==75||LA111_1==86||LA111_1==88||LA111_1==92||LA111_1==95||LA111_1==109||LA111_1==123||(LA111_1 >= 131 && LA111_1 <= 132)) ) {
					alt111=1;
				}
				else if ( (LA111_1==57) ) {
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
				break;
			case 98:
			case 130:
				{
				alt111=1;
				}
				break;
			case NAMED_PARAMETER:
			case 52:
			case 66:
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
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:391:14: general_identification_variable
					{
					pushFollow(FOLLOW_general_identification_variable_in_type_discriminator3529);
					general_identification_variable422=general_identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, general_identification_variable422.getTree());

					}
					break;
				case 2 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:391:48: path_expression
					{
					pushFollow(FOLLOW_path_expression_in_type_discriminator3533);
					path_expression423=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression423.getTree());

					}
					break;
				case 3 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:391:66: input_parameter
					{
					pushFollow(FOLLOW_input_parameter_in_type_discriminator3537);
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:392:1: functions_returning_numerics : ( 'LENGTH(' string_expression ')' | 'LOCATE(' string_expression ',' string_expression ( ',' arithmetic_expression )? ')' | 'ABS(' arithmetic_expression ')' | 'SQRT(' arithmetic_expression ')' | 'MOD(' arithmetic_expression ',' arithmetic_expression ')' | 'SIZE(' path_expression ')' | 'INDEX(' identification_variable ')' );
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
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:393:5: ( 'LENGTH(' string_expression ')' | 'LOCATE(' string_expression ',' string_expression ( ',' arithmetic_expression )? ')' | 'ABS(' arithmetic_expression ')' | 'SQRT(' arithmetic_expression ')' | 'MOD(' arithmetic_expression ',' arithmetic_expression ')' | 'SIZE(' path_expression ')' | 'INDEX(' identification_variable ')' )
			int alt113=7;
			switch ( input.LA(1) ) {
			case 100:
				{
				alt113=1;
				}
				break;
			case 102:
				{
				alt113=2;
				}
				break;
			case 72:
				{
				alt113=3;
				}
				break;
			case 121:
				{
				alt113=4;
				}
				break;
			case 106:
				{
				alt113=5;
				}
				break;
			case 119:
				{
				alt113=6;
				}
				break;
			case 96:
				{
				alt113=7;
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
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:393:7: 'LENGTH(' string_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal425=(Token)match(input,100,FOLLOW_100_in_functions_returning_numerics3549); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal425_tree = (Object)adaptor.create(string_literal425);
					adaptor.addChild(root_0, string_literal425_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_numerics3550);
					string_expression426=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression426.getTree());

					char_literal427=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3551); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal427_tree = (Object)adaptor.create(char_literal427);
					adaptor.addChild(root_0, char_literal427_tree);
					}

					}
					break;
				case 2 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:394:7: 'LOCATE(' string_expression ',' string_expression ( ',' arithmetic_expression )? ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal428=(Token)match(input,102,FOLLOW_102_in_functions_returning_numerics3559); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal428_tree = (Object)adaptor.create(string_literal428);
					adaptor.addChild(root_0, string_literal428_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_numerics3561);
					string_expression429=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression429.getTree());

					char_literal430=(Token)match(input,55,FOLLOW_55_in_functions_returning_numerics3562); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal430_tree = (Object)adaptor.create(char_literal430);
					adaptor.addChild(root_0, char_literal430_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_numerics3564);
					string_expression431=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression431.getTree());

					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:394:55: ( ',' arithmetic_expression )?
					int alt112=2;
					int LA112_0 = input.LA(1);
					if ( (LA112_0==55) ) {
						alt112=1;
					}
					switch (alt112) {
						case 1 :
							// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:394:56: ',' arithmetic_expression
							{
							char_literal432=(Token)match(input,55,FOLLOW_55_in_functions_returning_numerics3566); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							char_literal432_tree = (Object)adaptor.create(char_literal432);
							adaptor.addChild(root_0, char_literal432_tree);
							}

							pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_numerics3567);
							arithmetic_expression433=arithmetic_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression433.getTree());

							}
							break;

					}

					char_literal434=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3570); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal434_tree = (Object)adaptor.create(char_literal434);
					adaptor.addChild(root_0, char_literal434_tree);
					}

					}
					break;
				case 3 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:395:7: 'ABS(' arithmetic_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal435=(Token)match(input,72,FOLLOW_72_in_functions_returning_numerics3578); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal435_tree = (Object)adaptor.create(string_literal435);
					adaptor.addChild(root_0, string_literal435_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_numerics3579);
					arithmetic_expression436=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression436.getTree());

					char_literal437=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3580); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal437_tree = (Object)adaptor.create(char_literal437);
					adaptor.addChild(root_0, char_literal437_tree);
					}

					}
					break;
				case 4 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:396:7: 'SQRT(' arithmetic_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal438=(Token)match(input,121,FOLLOW_121_in_functions_returning_numerics3588); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal438_tree = (Object)adaptor.create(string_literal438);
					adaptor.addChild(root_0, string_literal438_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_numerics3589);
					arithmetic_expression439=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression439.getTree());

					char_literal440=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3590); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal440_tree = (Object)adaptor.create(char_literal440);
					adaptor.addChild(root_0, char_literal440_tree);
					}

					}
					break;
				case 5 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:397:7: 'MOD(' arithmetic_expression ',' arithmetic_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal441=(Token)match(input,106,FOLLOW_106_in_functions_returning_numerics3598); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal441_tree = (Object)adaptor.create(string_literal441);
					adaptor.addChild(root_0, string_literal441_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_numerics3599);
					arithmetic_expression442=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression442.getTree());

					char_literal443=(Token)match(input,55,FOLLOW_55_in_functions_returning_numerics3600); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal443_tree = (Object)adaptor.create(char_literal443);
					adaptor.addChild(root_0, char_literal443_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_numerics3602);
					arithmetic_expression444=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression444.getTree());

					char_literal445=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3603); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal445_tree = (Object)adaptor.create(char_literal445);
					adaptor.addChild(root_0, char_literal445_tree);
					}

					}
					break;
				case 6 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:398:7: 'SIZE(' path_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal446=(Token)match(input,119,FOLLOW_119_in_functions_returning_numerics3611); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal446_tree = (Object)adaptor.create(string_literal446);
					adaptor.addChild(root_0, string_literal446_tree);
					}

					pushFollow(FOLLOW_path_expression_in_functions_returning_numerics3612);
					path_expression447=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression447.getTree());

					char_literal448=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3613); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal448_tree = (Object)adaptor.create(char_literal448);
					adaptor.addChild(root_0, char_literal448_tree);
					}

					}
					break;
				case 7 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:399:7: 'INDEX(' identification_variable ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal449=(Token)match(input,96,FOLLOW_96_in_functions_returning_numerics3621); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal449_tree = (Object)adaptor.create(string_literal449);
					adaptor.addChild(root_0, string_literal449_tree);
					}

					pushFollow(FOLLOW_identification_variable_in_functions_returning_numerics3622);
					identification_variable450=identification_variable();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, identification_variable450.getTree());

					char_literal451=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_numerics3623); if (state.failed) return retval;
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:400:1: functions_returning_datetime : ( 'CURRENT_DATE' | 'CURRENT_TIME' | 'CURRENT_TIMESTAMP' );
	public final JPA2Parser.functions_returning_datetime_return functions_returning_datetime() throws RecognitionException {
		JPA2Parser.functions_returning_datetime_return retval = new JPA2Parser.functions_returning_datetime_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set452=null;

		Object set452_tree=null;

		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:401:5: ( 'CURRENT_DATE' | 'CURRENT_TIME' | 'CURRENT_TIMESTAMP' )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set452=input.LT(1);
			if ( (input.LA(1) >= 81 && input.LA(1) <= 83) ) {
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:404:1: functions_returning_strings : ( 'CONCAT(' string_expression ',' string_expression ( ',' string_expression )* ')' | 'SUBSTRING(' string_expression ',' arithmetic_expression ( ',' arithmetic_expression )? ')' | 'TRIM(' ( ( trim_specification )? ( trim_character )? 'FROM' )? string_expression ')' | 'LOWER(' string_expression ')' | 'UPPER(' string_expression ')' );
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
		Token char_literal475=null;
		Token string_literal476=null;
		Token char_literal478=null;
		ParserRuleReturnScope string_expression454 =null;
		ParserRuleReturnScope string_expression456 =null;
		ParserRuleReturnScope string_expression458 =null;
		ParserRuleReturnScope string_expression461 =null;
		ParserRuleReturnScope arithmetic_expression463 =null;
		ParserRuleReturnScope arithmetic_expression465 =null;
		ParserRuleReturnScope trim_specification468 =null;
		ParserRuleReturnScope trim_character469 =null;
		ParserRuleReturnScope string_expression471 =null;
		ParserRuleReturnScope string_expression474 =null;
		ParserRuleReturnScope string_expression477 =null;

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
		Object char_literal475_tree=null;
		Object string_literal476_tree=null;
		Object char_literal478_tree=null;

		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:405:5: ( 'CONCAT(' string_expression ',' string_expression ( ',' string_expression )* ')' | 'SUBSTRING(' string_expression ',' arithmetic_expression ( ',' arithmetic_expression )? ')' | 'TRIM(' ( ( trim_specification )? ( trim_character )? 'FROM' )? string_expression ')' | 'LOWER(' string_expression ')' | 'UPPER(' string_expression ')' )
			int alt119=5;
			switch ( input.LA(1) ) {
			case 80:
				{
				alt119=1;
				}
				break;
			case 122:
				{
				alt119=2;
				}
				break;
			case 126:
				{
				alt119=3;
				}
				break;
			case 103:
				{
				alt119=4;
				}
				break;
			case 129:
				{
				alt119=5;
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
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:405:7: 'CONCAT(' string_expression ',' string_expression ( ',' string_expression )* ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal453=(Token)match(input,80,FOLLOW_80_in_functions_returning_strings3661); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal453_tree = (Object)adaptor.create(string_literal453);
					adaptor.addChild(root_0, string_literal453_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_strings3662);
					string_expression454=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression454.getTree());

					char_literal455=(Token)match(input,55,FOLLOW_55_in_functions_returning_strings3663); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal455_tree = (Object)adaptor.create(char_literal455);
					adaptor.addChild(root_0, char_literal455_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_strings3665);
					string_expression456=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression456.getTree());

					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:405:55: ( ',' string_expression )*
					loop114:
					while (true) {
						int alt114=2;
						int LA114_0 = input.LA(1);
						if ( (LA114_0==55) ) {
							alt114=1;
						}

						switch (alt114) {
						case 1 :
							// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:405:56: ',' string_expression
							{
							char_literal457=(Token)match(input,55,FOLLOW_55_in_functions_returning_strings3668); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							char_literal457_tree = (Object)adaptor.create(char_literal457);
							adaptor.addChild(root_0, char_literal457_tree);
							}

							pushFollow(FOLLOW_string_expression_in_functions_returning_strings3670);
							string_expression458=string_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression458.getTree());

							}
							break;

						default :
							break loop114;
						}
					}

					char_literal459=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings3673); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal459_tree = (Object)adaptor.create(char_literal459);
					adaptor.addChild(root_0, char_literal459_tree);
					}

					}
					break;
				case 2 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:406:7: 'SUBSTRING(' string_expression ',' arithmetic_expression ( ',' arithmetic_expression )? ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal460=(Token)match(input,122,FOLLOW_122_in_functions_returning_strings3681); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal460_tree = (Object)adaptor.create(string_literal460);
					adaptor.addChild(root_0, string_literal460_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_strings3683);
					string_expression461=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression461.getTree());

					char_literal462=(Token)match(input,55,FOLLOW_55_in_functions_returning_strings3684); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal462_tree = (Object)adaptor.create(char_literal462);
					adaptor.addChild(root_0, char_literal462_tree);
					}

					pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_strings3686);
					arithmetic_expression463=arithmetic_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression463.getTree());

					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:406:63: ( ',' arithmetic_expression )?
					int alt115=2;
					int LA115_0 = input.LA(1);
					if ( (LA115_0==55) ) {
						alt115=1;
					}
					switch (alt115) {
						case 1 :
							// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:406:64: ',' arithmetic_expression
							{
							char_literal464=(Token)match(input,55,FOLLOW_55_in_functions_returning_strings3689); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							char_literal464_tree = (Object)adaptor.create(char_literal464);
							adaptor.addChild(root_0, char_literal464_tree);
							}

							pushFollow(FOLLOW_arithmetic_expression_in_functions_returning_strings3691);
							arithmetic_expression465=arithmetic_expression();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) adaptor.addChild(root_0, arithmetic_expression465.getTree());

							}
							break;

					}

					char_literal466=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings3694); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal466_tree = (Object)adaptor.create(char_literal466);
					adaptor.addChild(root_0, char_literal466_tree);
					}

					}
					break;
				case 3 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:407:7: 'TRIM(' ( ( trim_specification )? ( trim_character )? 'FROM' )? string_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal467=(Token)match(input,126,FOLLOW_126_in_functions_returning_strings3702); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal467_tree = (Object)adaptor.create(string_literal467);
					adaptor.addChild(root_0, string_literal467_tree);
					}

					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:407:14: ( ( trim_specification )? ( trim_character )? 'FROM' )?
					int alt118=2;
					int LA118_0 = input.LA(1);
					if ( (LA118_0==TRIM_CHARACTER||LA118_0==77||LA118_0==92||LA118_0==99||LA118_0==124) ) {
						alt118=1;
					}
					switch (alt118) {
						case 1 :
							// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:407:15: ( trim_specification )? ( trim_character )? 'FROM'
							{
							// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:407:15: ( trim_specification )?
							int alt116=2;
							int LA116_0 = input.LA(1);
							if ( (LA116_0==77||LA116_0==99||LA116_0==124) ) {
								alt116=1;
							}
							switch (alt116) {
								case 1 :
									// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:407:16: trim_specification
									{
									pushFollow(FOLLOW_trim_specification_in_functions_returning_strings3705);
									trim_specification468=trim_specification();
									state._fsp--;
									if (state.failed) return retval;
									if ( state.backtracking==0 ) adaptor.addChild(root_0, trim_specification468.getTree());

									}
									break;

							}

							// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:407:37: ( trim_character )?
							int alt117=2;
							int LA117_0 = input.LA(1);
							if ( (LA117_0==TRIM_CHARACTER) ) {
								alt117=1;
							}
							switch (alt117) {
								case 1 :
									// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:407:38: trim_character
									{
									pushFollow(FOLLOW_trim_character_in_functions_returning_strings3710);
									trim_character469=trim_character();
									state._fsp--;
									if (state.failed) return retval;
									if ( state.backtracking==0 ) adaptor.addChild(root_0, trim_character469.getTree());

									}
									break;

							}

							string_literal470=(Token)match(input,92,FOLLOW_92_in_functions_returning_strings3714); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							string_literal470_tree = (Object)adaptor.create(string_literal470);
							adaptor.addChild(root_0, string_literal470_tree);
							}

							}
							break;

					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_strings3718);
					string_expression471=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression471.getTree());

					char_literal472=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings3720); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal472_tree = (Object)adaptor.create(char_literal472);
					adaptor.addChild(root_0, char_literal472_tree);
					}

					}
					break;
				case 4 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:408:7: 'LOWER(' string_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal473=(Token)match(input,103,FOLLOW_103_in_functions_returning_strings3728); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal473_tree = (Object)adaptor.create(string_literal473);
					adaptor.addChild(root_0, string_literal473_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_strings3729);
					string_expression474=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression474.getTree());

					char_literal475=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings3730); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal475_tree = (Object)adaptor.create(char_literal475);
					adaptor.addChild(root_0, char_literal475_tree);
					}

					}
					break;
				case 5 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:409:7: 'UPPER(' string_expression ')'
					{
					root_0 = (Object)adaptor.nil();


					string_literal476=(Token)match(input,129,FOLLOW_129_in_functions_returning_strings3738); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal476_tree = (Object)adaptor.create(string_literal476);
					adaptor.addChild(root_0, string_literal476_tree);
					}

					pushFollow(FOLLOW_string_expression_in_functions_returning_strings3739);
					string_expression477=string_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, string_expression477.getTree());

					char_literal478=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_functions_returning_strings3740); if (state.failed) return retval;
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:410:1: trim_specification : ( 'LEADING' | 'TRAILING' | 'BOTH' );
	public final JPA2Parser.trim_specification_return trim_specification() throws RecognitionException {
		JPA2Parser.trim_specification_return retval = new JPA2Parser.trim_specification_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set479=null;

		Object set479_tree=null;

		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:411:5: ( 'LEADING' | 'TRAILING' | 'BOTH' )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:
			{
			root_0 = (Object)adaptor.nil();


			set479=input.LT(1);
			if ( input.LA(1)==77||input.LA(1)==99||input.LA(1)==124 ) {
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:412:1: function_invocation : 'FUNCTION(' function_name ( ',' function_arg )* ')' ;
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
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:413:5: ( 'FUNCTION(' function_name ( ',' function_arg )* ')' )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:413:7: 'FUNCTION(' function_name ( ',' function_arg )* ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal480=(Token)match(input,93,FOLLOW_93_in_function_invocation3770); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal480_tree = (Object)adaptor.create(string_literal480);
			adaptor.addChild(root_0, string_literal480_tree);
			}

			pushFollow(FOLLOW_function_name_in_function_invocation3771);
			function_name481=function_name();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, function_name481.getTree());

			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:413:32: ( ',' function_arg )*
			loop120:
			while (true) {
				int alt120=2;
				int LA120_0 = input.LA(1);
				if ( (LA120_0==55) ) {
					alt120=1;
				}

				switch (alt120) {
				case 1 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:413:33: ',' function_arg
					{
					char_literal482=(Token)match(input,55,FOLLOW_55_in_function_invocation3774); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal482_tree = (Object)adaptor.create(char_literal482);
					adaptor.addChild(root_0, char_literal482_tree);
					}

					pushFollow(FOLLOW_function_arg_in_function_invocation3776);
					function_arg483=function_arg();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, function_arg483.getTree());

					}
					break;

				default :
					break loop120;
				}
			}

			char_literal484=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_function_invocation3780); if (state.failed) return retval;
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:414:1: function_arg : ( literal | path_expression | input_parameter | scalar_expression );
	public final JPA2Parser.function_arg_return function_arg() throws RecognitionException {
		JPA2Parser.function_arg_return retval = new JPA2Parser.function_arg_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope literal485 =null;
		ParserRuleReturnScope path_expression486 =null;
		ParserRuleReturnScope input_parameter487 =null;
		ParserRuleReturnScope scalar_expression488 =null;


		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:415:5: ( literal | path_expression | input_parameter | scalar_expression )
			int alt121=4;
			switch ( input.LA(1) ) {
			case WORD:
				{
				int LA121_1 = input.LA(2);
				if ( (LA121_1==57) ) {
					alt121=2;
				}
				else if ( (synpred222_JPA2()) ) {
					alt121=1;
				}
				else if ( (true) ) {
					alt121=4;
				}

				}
				break;
			case 66:
				{
				int LA121_2 = input.LA(2);
				if ( (LA121_2==59) ) {
					int LA121_8 = input.LA(3);
					if ( (LA121_8==INT_NUMERAL) ) {
						int LA121_12 = input.LA(4);
						if ( (synpred224_JPA2()) ) {
							alt121=3;
						}
						else if ( (true) ) {
							alt121=4;
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
								new NoViableAltException("", 121, 8, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

				}
				else if ( (LA121_2==INT_NUMERAL) ) {
					int LA121_9 = input.LA(3);
					if ( (synpred224_JPA2()) ) {
						alt121=3;
					}
					else if ( (true) ) {
						alt121=4;
					}

				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 121, 2, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case NAMED_PARAMETER:
				{
				int LA121_3 = input.LA(2);
				if ( (synpred224_JPA2()) ) {
					alt121=3;
				}
				else if ( (true) ) {
					alt121=4;
				}

				}
				break;
			case 52:
				{
				int LA121_4 = input.LA(2);
				if ( (LA121_4==WORD) ) {
					int LA121_11 = input.LA(3);
					if ( (LA121_11==136) ) {
						int LA121_13 = input.LA(4);
						if ( (synpred224_JPA2()) ) {
							alt121=3;
						}
						else if ( (true) ) {
							alt121=4;
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
								new NoViableAltException("", 121, 11, input);
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
							new NoViableAltException("", 121, 4, input);
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
			case LPAREN:
			case MAX:
			case MIN:
			case STRING_LITERAL:
			case SUM:
			case 54:
			case 56:
			case 59:
			case 72:
			case 78:
			case 79:
			case 80:
			case 81:
			case 82:
			case 83:
			case 93:
			case 96:
			case 100:
			case 102:
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
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:415:7: literal
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_literal_in_function_arg3791);
					literal485=literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, literal485.getTree());

					}
					break;
				case 2 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:416:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_function_arg3799);
					path_expression486=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression486.getTree());

					}
					break;
				case 3 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:417:7: input_parameter
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_input_parameter_in_function_arg3807);
					input_parameter487=input_parameter();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, input_parameter487.getTree());

					}
					break;
				case 4 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:418:7: scalar_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_scalar_expression_in_function_arg3815);
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:419:1: case_expression : ( general_case_expression | simple_case_expression | coalesce_expression | nullif_expression );
	public final JPA2Parser.case_expression_return case_expression() throws RecognitionException {
		JPA2Parser.case_expression_return retval = new JPA2Parser.case_expression_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope general_case_expression489 =null;
		ParserRuleReturnScope simple_case_expression490 =null;
		ParserRuleReturnScope coalesce_expression491 =null;
		ParserRuleReturnScope nullif_expression492 =null;


		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:420:5: ( general_case_expression | simple_case_expression | coalesce_expression | nullif_expression )
			int alt122=4;
			switch ( input.LA(1) ) {
			case 78:
				{
				int LA122_1 = input.LA(2);
				if ( (LA122_1==131) ) {
					alt122=1;
				}
				else if ( (LA122_1==WORD||LA122_1==127) ) {
					alt122=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 122, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case 79:
				{
				alt122=3;
				}
				break;
			case 112:
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
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:420:7: general_case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_general_case_expression_in_case_expression3826);
					general_case_expression489=general_case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, general_case_expression489.getTree());

					}
					break;
				case 2 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:421:7: simple_case_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_simple_case_expression_in_case_expression3834);
					simple_case_expression490=simple_case_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_case_expression490.getTree());

					}
					break;
				case 3 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:422:7: coalesce_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_coalesce_expression_in_case_expression3842);
					coalesce_expression491=coalesce_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, coalesce_expression491.getTree());

					}
					break;
				case 4 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:423:7: nullif_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_nullif_expression_in_case_expression3850);
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:424:1: general_case_expression : 'CASE' when_clause ( when_clause )* 'ELSE' scalar_expression 'END' ;
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
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:425:5: ( 'CASE' when_clause ( when_clause )* 'ELSE' scalar_expression 'END' )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:425:7: 'CASE' when_clause ( when_clause )* 'ELSE' scalar_expression 'END'
			{
			root_0 = (Object)adaptor.nil();


			string_literal493=(Token)match(input,78,FOLLOW_78_in_general_case_expression3861); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal493_tree = (Object)adaptor.create(string_literal493);
			adaptor.addChild(root_0, string_literal493_tree);
			}

			pushFollow(FOLLOW_when_clause_in_general_case_expression3863);
			when_clause494=when_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, when_clause494.getTree());

			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:425:26: ( when_clause )*
			loop123:
			while (true) {
				int alt123=2;
				int LA123_0 = input.LA(1);
				if ( (LA123_0==131) ) {
					alt123=1;
				}

				switch (alt123) {
				case 1 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:425:27: when_clause
					{
					pushFollow(FOLLOW_when_clause_in_general_case_expression3866);
					when_clause495=when_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, when_clause495.getTree());

					}
					break;

				default :
					break loop123;
				}
			}

			string_literal496=(Token)match(input,86,FOLLOW_86_in_general_case_expression3870); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal496_tree = (Object)adaptor.create(string_literal496);
			adaptor.addChild(root_0, string_literal496_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_general_case_expression3872);
			scalar_expression497=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression497.getTree());

			string_literal498=(Token)match(input,88,FOLLOW_88_in_general_case_expression3874); if (state.failed) return retval;
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:426:1: when_clause : 'WHEN' conditional_expression 'THEN' scalar_expression ;
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
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:427:5: ( 'WHEN' conditional_expression 'THEN' scalar_expression )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:427:7: 'WHEN' conditional_expression 'THEN' scalar_expression
			{
			root_0 = (Object)adaptor.nil();


			string_literal499=(Token)match(input,131,FOLLOW_131_in_when_clause3885); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal499_tree = (Object)adaptor.create(string_literal499);
			adaptor.addChild(root_0, string_literal499_tree);
			}

			pushFollow(FOLLOW_conditional_expression_in_when_clause3887);
			conditional_expression500=conditional_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, conditional_expression500.getTree());

			string_literal501=(Token)match(input,123,FOLLOW_123_in_when_clause3889); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal501_tree = (Object)adaptor.create(string_literal501);
			adaptor.addChild(root_0, string_literal501_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_when_clause3891);
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:428:1: simple_case_expression : 'CASE' case_operand simple_when_clause ( simple_when_clause )* 'ELSE' scalar_expression 'END' ;
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
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:429:5: ( 'CASE' case_operand simple_when_clause ( simple_when_clause )* 'ELSE' scalar_expression 'END' )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:429:7: 'CASE' case_operand simple_when_clause ( simple_when_clause )* 'ELSE' scalar_expression 'END'
			{
			root_0 = (Object)adaptor.nil();


			string_literal503=(Token)match(input,78,FOLLOW_78_in_simple_case_expression3902); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal503_tree = (Object)adaptor.create(string_literal503);
			adaptor.addChild(root_0, string_literal503_tree);
			}

			pushFollow(FOLLOW_case_operand_in_simple_case_expression3904);
			case_operand504=case_operand();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, case_operand504.getTree());

			pushFollow(FOLLOW_simple_when_clause_in_simple_case_expression3906);
			simple_when_clause505=simple_when_clause();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_when_clause505.getTree());

			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:429:46: ( simple_when_clause )*
			loop124:
			while (true) {
				int alt124=2;
				int LA124_0 = input.LA(1);
				if ( (LA124_0==131) ) {
					alt124=1;
				}

				switch (alt124) {
				case 1 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:429:47: simple_when_clause
					{
					pushFollow(FOLLOW_simple_when_clause_in_simple_case_expression3909);
					simple_when_clause506=simple_when_clause();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, simple_when_clause506.getTree());

					}
					break;

				default :
					break loop124;
				}
			}

			string_literal507=(Token)match(input,86,FOLLOW_86_in_simple_case_expression3913); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal507_tree = (Object)adaptor.create(string_literal507);
			adaptor.addChild(root_0, string_literal507_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_simple_case_expression3915);
			scalar_expression508=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression508.getTree());

			string_literal509=(Token)match(input,88,FOLLOW_88_in_simple_case_expression3917); if (state.failed) return retval;
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:430:1: case_operand : ( path_expression | type_discriminator );
	public final JPA2Parser.case_operand_return case_operand() throws RecognitionException {
		JPA2Parser.case_operand_return retval = new JPA2Parser.case_operand_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope path_expression510 =null;
		ParserRuleReturnScope type_discriminator511 =null;


		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:431:5: ( path_expression | type_discriminator )
			int alt125=2;
			int LA125_0 = input.LA(1);
			if ( (LA125_0==WORD) ) {
				alt125=1;
			}
			else if ( (LA125_0==127) ) {
				alt125=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 125, 0, input);
				throw nvae;
			}

			switch (alt125) {
				case 1 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:431:7: path_expression
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_path_expression_in_case_operand3928);
					path_expression510=path_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, path_expression510.getTree());

					}
					break;
				case 2 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:432:7: type_discriminator
					{
					root_0 = (Object)adaptor.nil();


					pushFollow(FOLLOW_type_discriminator_in_case_operand3936);
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:433:1: simple_when_clause : 'WHEN' scalar_expression 'THEN' scalar_expression ;
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
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:434:5: ( 'WHEN' scalar_expression 'THEN' scalar_expression )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:434:7: 'WHEN' scalar_expression 'THEN' scalar_expression
			{
			root_0 = (Object)adaptor.nil();


			string_literal512=(Token)match(input,131,FOLLOW_131_in_simple_when_clause3947); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal512_tree = (Object)adaptor.create(string_literal512);
			adaptor.addChild(root_0, string_literal512_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_simple_when_clause3949);
			scalar_expression513=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression513.getTree());

			string_literal514=(Token)match(input,123,FOLLOW_123_in_simple_when_clause3951); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal514_tree = (Object)adaptor.create(string_literal514);
			adaptor.addChild(root_0, string_literal514_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_simple_when_clause3953);
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:435:1: coalesce_expression : 'COALESCE(' scalar_expression ( ',' scalar_expression )+ ')' ;
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
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:436:5: ( 'COALESCE(' scalar_expression ( ',' scalar_expression )+ ')' )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:436:7: 'COALESCE(' scalar_expression ( ',' scalar_expression )+ ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal516=(Token)match(input,79,FOLLOW_79_in_coalesce_expression3964); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal516_tree = (Object)adaptor.create(string_literal516);
			adaptor.addChild(root_0, string_literal516_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_coalesce_expression3965);
			scalar_expression517=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression517.getTree());

			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:436:36: ( ',' scalar_expression )+
			int cnt126=0;
			loop126:
			while (true) {
				int alt126=2;
				int LA126_0 = input.LA(1);
				if ( (LA126_0==55) ) {
					alt126=1;
				}

				switch (alt126) {
				case 1 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:436:37: ',' scalar_expression
					{
					char_literal518=(Token)match(input,55,FOLLOW_55_in_coalesce_expression3968); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal518_tree = (Object)adaptor.create(char_literal518);
					adaptor.addChild(root_0, char_literal518_tree);
					}

					pushFollow(FOLLOW_scalar_expression_in_coalesce_expression3970);
					scalar_expression519=scalar_expression();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression519.getTree());

					}
					break;

				default :
					if ( cnt126 >= 1 ) break loop126;
					if (state.backtracking>0) {state.failed=true; return retval;}
					EarlyExitException eee = new EarlyExitException(126, input);
					throw eee;
				}
				cnt126++;
			}

			char_literal520=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_coalesce_expression3973); if (state.failed) return retval;
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:437:1: nullif_expression : 'NULLIF(' scalar_expression ',' scalar_expression ')' ;
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
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:438:5: ( 'NULLIF(' scalar_expression ',' scalar_expression ')' )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:438:7: 'NULLIF(' scalar_expression ',' scalar_expression ')'
			{
			root_0 = (Object)adaptor.nil();


			string_literal521=(Token)match(input,112,FOLLOW_112_in_nullif_expression3984); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			string_literal521_tree = (Object)adaptor.create(string_literal521);
			adaptor.addChild(root_0, string_literal521_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_nullif_expression3985);
			scalar_expression522=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression522.getTree());

			char_literal523=(Token)match(input,55,FOLLOW_55_in_nullif_expression3987); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			char_literal523_tree = (Object)adaptor.create(char_literal523);
			adaptor.addChild(root_0, char_literal523_tree);
			}

			pushFollow(FOLLOW_scalar_expression_in_nullif_expression3989);
			scalar_expression524=scalar_expression();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, scalar_expression524.getTree());

			char_literal525=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_nullif_expression3990); if (state.failed) return retval;
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:441:1: input_parameter : ( '?' numeric_literal -> ^( T_PARAMETER[] '?' numeric_literal ) | NAMED_PARAMETER -> ^( T_PARAMETER[] NAMED_PARAMETER ) | '${' WORD '}' -> ^( T_PARAMETER[] '${' WORD '}' ) );
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
		RewriteRuleTokenStream stream_WORD=new RewriteRuleTokenStream(adaptor,"token WORD");
		RewriteRuleTokenStream stream_66=new RewriteRuleTokenStream(adaptor,"token 66");
		RewriteRuleTokenStream stream_NAMED_PARAMETER=new RewriteRuleTokenStream(adaptor,"token NAMED_PARAMETER");
		RewriteRuleTokenStream stream_136=new RewriteRuleTokenStream(adaptor,"token 136");
		RewriteRuleTokenStream stream_52=new RewriteRuleTokenStream(adaptor,"token 52");
		RewriteRuleSubtreeStream stream_numeric_literal=new RewriteRuleSubtreeStream(adaptor,"rule numeric_literal");

		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:442:5: ( '?' numeric_literal -> ^( T_PARAMETER[] '?' numeric_literal ) | NAMED_PARAMETER -> ^( T_PARAMETER[] NAMED_PARAMETER ) | '${' WORD '}' -> ^( T_PARAMETER[] '${' WORD '}' ) )
			int alt127=3;
			switch ( input.LA(1) ) {
			case 66:
				{
				alt127=1;
				}
				break;
			case NAMED_PARAMETER:
				{
				alt127=2;
				}
				break;
			case 52:
				{
				alt127=3;
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
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:442:7: '?' numeric_literal
					{
					char_literal526=(Token)match(input,66,FOLLOW_66_in_input_parameter4003); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_66.add(char_literal526);

					pushFollow(FOLLOW_numeric_literal_in_input_parameter4005);
					numeric_literal527=numeric_literal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_numeric_literal.add(numeric_literal527.getTree());
					// AST REWRITE
					// elements: numeric_literal, 66
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 442:27: -> ^( T_PARAMETER[] '?' numeric_literal )
					{
						// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:442:30: ^( T_PARAMETER[] '?' numeric_literal )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new ParameterNode(T_PARAMETER), root_1);
						adaptor.addChild(root_1, stream_66.nextNode());
						adaptor.addChild(root_1, stream_numeric_literal.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 2 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:443:7: NAMED_PARAMETER
					{
					NAMED_PARAMETER528=(Token)match(input,NAMED_PARAMETER,FOLLOW_NAMED_PARAMETER_in_input_parameter4028); if (state.failed) return retval; 
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
					// 443:23: -> ^( T_PARAMETER[] NAMED_PARAMETER )
					{
						// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:443:26: ^( T_PARAMETER[] NAMED_PARAMETER )
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
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:444:7: '${' WORD '}'
					{
					string_literal529=(Token)match(input,52,FOLLOW_52_in_input_parameter4049); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_52.add(string_literal529);

					WORD530=(Token)match(input,WORD,FOLLOW_WORD_in_input_parameter4051); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_WORD.add(WORD530);

					char_literal531=(Token)match(input,136,FOLLOW_136_in_input_parameter4053); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_136.add(char_literal531);

					// AST REWRITE
					// elements: 136, WORD, 52
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (Object)adaptor.nil();
					// 444:21: -> ^( T_PARAMETER[] '${' WORD '}' )
					{
						// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:444:24: ^( T_PARAMETER[] '${' WORD '}' )
						{
						Object root_1 = (Object)adaptor.nil();
						root_1 = (Object)adaptor.becomeRoot(new ParameterNode(T_PARAMETER), root_1);
						adaptor.addChild(root_1, stream_52.nextNode());
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:446:1: literal : WORD ;
	public final JPA2Parser.literal_return literal() throws RecognitionException {
		JPA2Parser.literal_return retval = new JPA2Parser.literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD532=null;

		Object WORD532_tree=null;

		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:447:5: ( WORD )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:447:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD532=(Token)match(input,WORD,FOLLOW_WORD_in_literal4081); if (state.failed) return retval;
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:449:1: constructor_name : WORD ;
	public final JPA2Parser.constructor_name_return constructor_name() throws RecognitionException {
		JPA2Parser.constructor_name_return retval = new JPA2Parser.constructor_name_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD533=null;

		Object WORD533_tree=null;

		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:450:5: ( WORD )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:450:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD533=(Token)match(input,WORD,FOLLOW_WORD_in_constructor_name4093); if (state.failed) return retval;
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:452:1: enum_literal : WORD ;
	public final JPA2Parser.enum_literal_return enum_literal() throws RecognitionException {
		JPA2Parser.enum_literal_return retval = new JPA2Parser.enum_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD534=null;

		Object WORD534_tree=null;

		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:453:5: ( WORD )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:453:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD534=(Token)match(input,WORD,FOLLOW_WORD_in_enum_literal4105); if (state.failed) return retval;
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:455:1: boolean_literal : ( 'true' | 'false' );
	public final JPA2Parser.boolean_literal_return boolean_literal() throws RecognitionException {
		JPA2Parser.boolean_literal_return retval = new JPA2Parser.boolean_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set535=null;

		Object set535_tree=null;

		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:456:5: ( 'true' | 'false' )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:460:1: field : ( WORD | 'GROUP' );
	public final JPA2Parser.field_return field() throws RecognitionException {
		JPA2Parser.field_return retval = new JPA2Parser.field_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token set536=null;

		Object set536_tree=null;

		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:461:5: ( WORD | 'GROUP' )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:463:1: identification_variable : WORD ;
	public final JPA2Parser.identification_variable_return identification_variable() throws RecognitionException {
		JPA2Parser.identification_variable_return retval = new JPA2Parser.identification_variable_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD537=null;

		Object WORD537_tree=null;

		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:464:5: ( WORD )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:464:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD537=(Token)match(input,WORD,FOLLOW_WORD_in_identification_variable4154); if (state.failed) return retval;
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:466:1: parameter_name : WORD ( '.' WORD )* ;
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
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:467:5: ( WORD ( '.' WORD )* )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:467:7: WORD ( '.' WORD )*
			{
			root_0 = (Object)adaptor.nil();


			WORD538=(Token)match(input,WORD,FOLLOW_WORD_in_parameter_name4166); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			WORD538_tree = (Object)adaptor.create(WORD538);
			adaptor.addChild(root_0, WORD538_tree);
			}

			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:467:12: ( '.' WORD )*
			loop128:
			while (true) {
				int alt128=2;
				int LA128_0 = input.LA(1);
				if ( (LA128_0==57) ) {
					alt128=1;
				}

				switch (alt128) {
				case 1 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:467:13: '.' WORD
					{
					char_literal539=(Token)match(input,57,FOLLOW_57_in_parameter_name4169); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					char_literal539_tree = (Object)adaptor.create(char_literal539);
					adaptor.addChild(root_0, char_literal539_tree);
					}

					WORD540=(Token)match(input,WORD,FOLLOW_WORD_in_parameter_name4172); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					WORD540_tree = (Object)adaptor.create(WORD540);
					adaptor.addChild(root_0, WORD540_tree);
					}

					}
					break;

				default :
					break loop128;
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:469:1: escape_character : ESCAPE_CHARACTER ;
	public final JPA2Parser.escape_character_return escape_character() throws RecognitionException {
		JPA2Parser.escape_character_return retval = new JPA2Parser.escape_character_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token ESCAPE_CHARACTER541=null;

		Object ESCAPE_CHARACTER541_tree=null;

		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:470:5: ( ESCAPE_CHARACTER )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:470:7: ESCAPE_CHARACTER
			{
			root_0 = (Object)adaptor.nil();


			ESCAPE_CHARACTER541=(Token)match(input,ESCAPE_CHARACTER,FOLLOW_ESCAPE_CHARACTER_in_escape_character4186); if (state.failed) return retval;
			if ( state.backtracking==0 ) {
			ESCAPE_CHARACTER541_tree = (Object)adaptor.create(ESCAPE_CHARACTER541);
			adaptor.addChild(root_0, ESCAPE_CHARACTER541_tree);
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (Object)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:471:1: trim_character : TRIM_CHARACTER ;
	public final JPA2Parser.trim_character_return trim_character() throws RecognitionException {
		JPA2Parser.trim_character_return retval = new JPA2Parser.trim_character_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token TRIM_CHARACTER542=null;

		Object TRIM_CHARACTER542_tree=null;

		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:472:5: ( TRIM_CHARACTER )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:472:7: TRIM_CHARACTER
			{
			root_0 = (Object)adaptor.nil();


			TRIM_CHARACTER542=(Token)match(input,TRIM_CHARACTER,FOLLOW_TRIM_CHARACTER_in_trim_character4197); if (state.failed) return retval;
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:473:1: string_literal : STRING_LITERAL ;
	public final JPA2Parser.string_literal_return string_literal() throws RecognitionException {
		JPA2Parser.string_literal_return retval = new JPA2Parser.string_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token STRING_LITERAL543=null;

		Object STRING_LITERAL543_tree=null;

		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:474:5: ( STRING_LITERAL )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:474:7: STRING_LITERAL
			{
			root_0 = (Object)adaptor.nil();


			STRING_LITERAL543=(Token)match(input,STRING_LITERAL,FOLLOW_STRING_LITERAL_in_string_literal4208); if (state.failed) return retval;
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:475:1: numeric_literal : ( '0x' )? INT_NUMERAL ;
	public final JPA2Parser.numeric_literal_return numeric_literal() throws RecognitionException {
		JPA2Parser.numeric_literal_return retval = new JPA2Parser.numeric_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token string_literal544=null;
		Token INT_NUMERAL545=null;

		Object string_literal544_tree=null;
		Object INT_NUMERAL545_tree=null;

		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:476:5: ( ( '0x' )? INT_NUMERAL )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:476:7: ( '0x' )? INT_NUMERAL
			{
			root_0 = (Object)adaptor.nil();


			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:476:7: ( '0x' )?
			int alt129=2;
			int LA129_0 = input.LA(1);
			if ( (LA129_0==59) ) {
				alt129=1;
			}
			switch (alt129) {
				case 1 :
					// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:476:8: '0x'
					{
					string_literal544=(Token)match(input,59,FOLLOW_59_in_numeric_literal4220); if (state.failed) return retval;
					if ( state.backtracking==0 ) {
					string_literal544_tree = (Object)adaptor.create(string_literal544);
					adaptor.addChild(root_0, string_literal544_tree);
					}

					}
					break;

			}

			INT_NUMERAL545=(Token)match(input,INT_NUMERAL,FOLLOW_INT_NUMERAL_in_numeric_literal4224); if (state.failed) return retval;
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:477:1: single_valued_object_field : WORD ;
	public final JPA2Parser.single_valued_object_field_return single_valued_object_field() throws RecognitionException {
		JPA2Parser.single_valued_object_field_return retval = new JPA2Parser.single_valued_object_field_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD546=null;

		Object WORD546_tree=null;

		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:478:5: ( WORD )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:478:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD546=(Token)match(input,WORD,FOLLOW_WORD_in_single_valued_object_field4236); if (state.failed) return retval;
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:479:1: single_valued_embeddable_object_field : WORD ;
	public final JPA2Parser.single_valued_embeddable_object_field_return single_valued_embeddable_object_field() throws RecognitionException {
		JPA2Parser.single_valued_embeddable_object_field_return retval = new JPA2Parser.single_valued_embeddable_object_field_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD547=null;

		Object WORD547_tree=null;

		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:480:5: ( WORD )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:480:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD547=(Token)match(input,WORD,FOLLOW_WORD_in_single_valued_embeddable_object_field4247); if (state.failed) return retval;
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:481:1: collection_valued_field : WORD ;
	public final JPA2Parser.collection_valued_field_return collection_valued_field() throws RecognitionException {
		JPA2Parser.collection_valued_field_return retval = new JPA2Parser.collection_valued_field_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD548=null;

		Object WORD548_tree=null;

		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:482:5: ( WORD )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:482:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD548=(Token)match(input,WORD,FOLLOW_WORD_in_collection_valued_field4258); if (state.failed) return retval;
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:483:1: entity_name : WORD ;
	public final JPA2Parser.entity_name_return entity_name() throws RecognitionException {
		JPA2Parser.entity_name_return retval = new JPA2Parser.entity_name_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD549=null;

		Object WORD549_tree=null;

		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:484:5: ( WORD )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:484:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD549=(Token)match(input,WORD,FOLLOW_WORD_in_entity_name4269); if (state.failed) return retval;
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:485:1: subtype : WORD ;
	public final JPA2Parser.subtype_return subtype() throws RecognitionException {
		JPA2Parser.subtype_return retval = new JPA2Parser.subtype_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD550=null;

		Object WORD550_tree=null;

		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:486:5: ( WORD )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:486:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD550=(Token)match(input,WORD,FOLLOW_WORD_in_subtype4280); if (state.failed) return retval;
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:487:1: entity_type_literal : WORD ;
	public final JPA2Parser.entity_type_literal_return entity_type_literal() throws RecognitionException {
		JPA2Parser.entity_type_literal_return retval = new JPA2Parser.entity_type_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD551=null;

		Object WORD551_tree=null;

		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:488:5: ( WORD )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:488:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD551=(Token)match(input,WORD,FOLLOW_WORD_in_entity_type_literal4291); if (state.failed) return retval;
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:489:1: function_name : WORD ;
	public final JPA2Parser.function_name_return function_name() throws RecognitionException {
		JPA2Parser.function_name_return retval = new JPA2Parser.function_name_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD552=null;

		Object WORD552_tree=null;

		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:490:5: ( WORD )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:490:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD552=(Token)match(input,WORD,FOLLOW_WORD_in_function_name4302); if (state.failed) return retval;
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
	// $ANTLR end "function_name"


	public static class state_field_return extends ParserRuleReturnScope {
		Object tree;
		@Override
		public Object getTree() { return tree; }
	};


	// $ANTLR start "state_field"
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:491:1: state_field : WORD ;
	public final JPA2Parser.state_field_return state_field() throws RecognitionException {
		JPA2Parser.state_field_return retval = new JPA2Parser.state_field_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD553=null;

		Object WORD553_tree=null;

		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:492:5: ( WORD )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:492:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD553=(Token)match(input,WORD,FOLLOW_WORD_in_state_field4313); if (state.failed) return retval;
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:493:1: result_variable : WORD ;
	public final JPA2Parser.result_variable_return result_variable() throws RecognitionException {
		JPA2Parser.result_variable_return retval = new JPA2Parser.result_variable_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD554=null;

		Object WORD554_tree=null;

		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:494:5: ( WORD )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:494:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD554=(Token)match(input,WORD,FOLLOW_WORD_in_result_variable4324); if (state.failed) return retval;
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:495:1: superquery_identification_variable : WORD ;
	public final JPA2Parser.superquery_identification_variable_return superquery_identification_variable() throws RecognitionException {
		JPA2Parser.superquery_identification_variable_return retval = new JPA2Parser.superquery_identification_variable_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD555=null;

		Object WORD555_tree=null;

		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:496:5: ( WORD )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:496:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD555=(Token)match(input,WORD,FOLLOW_WORD_in_superquery_identification_variable4335); if (state.failed) return retval;
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:497:1: date_time_timestamp_literal : WORD ;
	public final JPA2Parser.date_time_timestamp_literal_return date_time_timestamp_literal() throws RecognitionException {
		JPA2Parser.date_time_timestamp_literal_return retval = new JPA2Parser.date_time_timestamp_literal_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token WORD556=null;

		Object WORD556_tree=null;

		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:498:5: ( WORD )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:498:7: WORD
			{
			root_0 = (Object)adaptor.nil();


			WORD556=(Token)match(input,WORD,FOLLOW_WORD_in_date_time_timestamp_literal4346); if (state.failed) return retval;
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:499:1: pattern_value : string_literal ;
	public final JPA2Parser.pattern_value_return pattern_value() throws RecognitionException {
		JPA2Parser.pattern_value_return retval = new JPA2Parser.pattern_value_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope string_literal557 =null;


		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:500:5: ( string_literal )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:500:7: string_literal
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_string_literal_in_pattern_value4357);
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:501:1: collection_valued_input_parameter : input_parameter ;
	public final JPA2Parser.collection_valued_input_parameter_return collection_valued_input_parameter() throws RecognitionException {
		JPA2Parser.collection_valued_input_parameter_return retval = new JPA2Parser.collection_valued_input_parameter_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope input_parameter558 =null;


		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:502:5: ( input_parameter )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:502:7: input_parameter
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_input_parameter_in_collection_valued_input_parameter4368);
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
	// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:503:1: single_valued_input_parameter : input_parameter ;
	public final JPA2Parser.single_valued_input_parameter_return single_valued_input_parameter() throws RecognitionException {
		JPA2Parser.single_valued_input_parameter_return retval = new JPA2Parser.single_valued_input_parameter_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		ParserRuleReturnScope input_parameter559 =null;


		try {
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:504:5: ( input_parameter )
			// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:504:7: input_parameter
			{
			root_0 = (Object)adaptor.nil();


			pushFollow(FOLLOW_input_parameter_in_single_valued_input_parameter4379);
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

	// $ANTLR start synpred30_JPA2
	public final void synpred30_JPA2_fragment() throws RecognitionException {
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:144:7: ( scalar_expression )
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:144:7: scalar_expression
		{
		pushFollow(FOLLOW_scalar_expression_in_synpred30_JPA21113);
		scalar_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred30_JPA2

	// $ANTLR start synpred31_JPA2
	public final void synpred31_JPA2_fragment() throws RecognitionException {
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:145:7: ( simple_entity_expression )
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:145:7: simple_entity_expression
		{
		pushFollow(FOLLOW_simple_entity_expression_in_synpred31_JPA21121);
		simple_entity_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred31_JPA2

	// $ANTLR start synpred38_JPA2
	public final void synpred38_JPA2_fragment() throws RecognitionException {
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:155:7: ( path_expression )
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:155:7: path_expression
		{
		pushFollow(FOLLOW_path_expression_in_synpred38_JPA21231);
		path_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred38_JPA2

	// $ANTLR start synpred39_JPA2
	public final void synpred39_JPA2_fragment() throws RecognitionException {
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:156:7: ( identification_variable )
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:156:7: identification_variable
		{
		pushFollow(FOLLOW_identification_variable_in_synpred39_JPA21239);
		identification_variable();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred39_JPA2

	// $ANTLR start synpred40_JPA2
	public final void synpred40_JPA2_fragment() throws RecognitionException {
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:157:7: ( scalar_expression )
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:157:7: scalar_expression
		{
		pushFollow(FOLLOW_scalar_expression_in_synpred40_JPA21257);
		scalar_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred40_JPA2

	// $ANTLR start synpred41_JPA2
	public final void synpred41_JPA2_fragment() throws RecognitionException {
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:158:7: ( aggregate_expression )
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:158:7: aggregate_expression
		{
		pushFollow(FOLLOW_aggregate_expression_in_synpred41_JPA21265);
		aggregate_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred41_JPA2

	// $ANTLR start synpred44_JPA2
	public final void synpred44_JPA2_fragment() throws RecognitionException {
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:164:7: ( path_expression )
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:164:7: path_expression
		{
		pushFollow(FOLLOW_path_expression_in_synpred44_JPA21322);
		path_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred44_JPA2

	// $ANTLR start synpred45_JPA2
	public final void synpred45_JPA2_fragment() throws RecognitionException {
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:165:7: ( scalar_expression )
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:165:7: scalar_expression
		{
		pushFollow(FOLLOW_scalar_expression_in_synpred45_JPA21330);
		scalar_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred45_JPA2

	// $ANTLR start synpred46_JPA2
	public final void synpred46_JPA2_fragment() throws RecognitionException {
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:166:7: ( aggregate_expression )
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:166:7: aggregate_expression
		{
		pushFollow(FOLLOW_aggregate_expression_in_synpred46_JPA21338);
		aggregate_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred46_JPA2

	// $ANTLR start synpred48_JPA2
	public final void synpred48_JPA2_fragment() throws RecognitionException {
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:169:7: ( aggregate_expression_function_name '(' ( DISTINCT )? path_expression ')' )
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:169:7: aggregate_expression_function_name '(' ( DISTINCT )? path_expression ')'
		{
		pushFollow(FOLLOW_aggregate_expression_function_name_in_synpred48_JPA21357);
		aggregate_expression_function_name();
		state._fsp--;
		if (state.failed) return;

		match(input,LPAREN,FOLLOW_LPAREN_in_synpred48_JPA21359); if (state.failed) return;

		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:169:45: ( DISTINCT )?
		int alt135=2;
		int LA135_0 = input.LA(1);
		if ( (LA135_0==DISTINCT) ) {
			alt135=1;
		}
		switch (alt135) {
			case 1 :
				// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:169:46: DISTINCT
				{
				match(input,DISTINCT,FOLLOW_DISTINCT_in_synpred48_JPA21361); if (state.failed) return;

				}
				break;

		}

		pushFollow(FOLLOW_path_expression_in_synpred48_JPA21365);
		path_expression();
		state._fsp--;
		if (state.failed) return;

		match(input,RPAREN,FOLLOW_RPAREN_in_synpred48_JPA21366); if (state.failed) return;

		}

	}
	// $ANTLR end synpred48_JPA2

	// $ANTLR start synpred50_JPA2
	public final void synpred50_JPA2_fragment() throws RecognitionException {
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:171:7: ( 'COUNT' '(' ( DISTINCT )? count_argument ')' )
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:171:7: 'COUNT' '(' ( DISTINCT )? count_argument ')'
		{
		match(input,COUNT,FOLLOW_COUNT_in_synpred50_JPA21400); if (state.failed) return;

		match(input,LPAREN,FOLLOW_LPAREN_in_synpred50_JPA21402); if (state.failed) return;

		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:171:18: ( DISTINCT )?
		int alt136=2;
		int LA136_0 = input.LA(1);
		if ( (LA136_0==DISTINCT) ) {
			alt136=1;
		}
		switch (alt136) {
			case 1 :
				// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:171:19: DISTINCT
				{
				match(input,DISTINCT,FOLLOW_DISTINCT_in_synpred50_JPA21404); if (state.failed) return;

				}
				break;

		}

		pushFollow(FOLLOW_count_argument_in_synpred50_JPA21408);
		count_argument();
		state._fsp--;
		if (state.failed) return;

		match(input,RPAREN,FOLLOW_RPAREN_in_synpred50_JPA21410); if (state.failed) return;

		}

	}
	// $ANTLR end synpred50_JPA2

	// $ANTLR start synpred62_JPA2
	public final void synpred62_JPA2_fragment() throws RecognitionException {
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:196:25: ( general_identification_variable )
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:196:25: general_identification_variable
		{
		pushFollow(FOLLOW_general_identification_variable_in_synpred62_JPA21708);
		general_identification_variable();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred62_JPA2

	// $ANTLR start synpred70_JPA2
	public final void synpred70_JPA2_fragment() throws RecognitionException {
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:210:7: ( general_derived_path '.' single_valued_object_field )
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:210:7: general_derived_path '.' single_valued_object_field
		{
		pushFollow(FOLLOW_general_derived_path_in_synpred70_JPA21882);
		general_derived_path();
		state._fsp--;
		if (state.failed) return;

		match(input,57,FOLLOW_57_in_synpred70_JPA21883); if (state.failed) return;

		pushFollow(FOLLOW_single_valued_object_field_in_synpred70_JPA21884);
		single_valued_object_field();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred70_JPA2

	// $ANTLR start synpred75_JPA2
	public final void synpred75_JPA2_fragment() throws RecognitionException {
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:228:7: ( path_expression )
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:228:7: path_expression
		{
		pushFollow(FOLLOW_path_expression_in_synpred75_JPA22032);
		path_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred75_JPA2

	// $ANTLR start synpred76_JPA2
	public final void synpred76_JPA2_fragment() throws RecognitionException {
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:229:7: ( scalar_expression )
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:229:7: scalar_expression
		{
		pushFollow(FOLLOW_scalar_expression_in_synpred76_JPA22040);
		scalar_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred76_JPA2

	// $ANTLR start synpred77_JPA2
	public final void synpred77_JPA2_fragment() throws RecognitionException {
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:230:7: ( aggregate_expression )
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:230:7: aggregate_expression
		{
		pushFollow(FOLLOW_aggregate_expression_in_synpred77_JPA22048);
		aggregate_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred77_JPA2

	// $ANTLR start synpred78_JPA2
	public final void synpred78_JPA2_fragment() throws RecognitionException {
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:233:7: ( arithmetic_expression )
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:233:7: arithmetic_expression
		{
		pushFollow(FOLLOW_arithmetic_expression_in_synpred78_JPA22067);
		arithmetic_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred78_JPA2

	// $ANTLR start synpred79_JPA2
	public final void synpred79_JPA2_fragment() throws RecognitionException {
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:234:7: ( string_expression )
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:234:7: string_expression
		{
		pushFollow(FOLLOW_string_expression_in_synpred79_JPA22075);
		string_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred79_JPA2

	// $ANTLR start synpred80_JPA2
	public final void synpred80_JPA2_fragment() throws RecognitionException {
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:235:7: ( enum_expression )
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:235:7: enum_expression
		{
		pushFollow(FOLLOW_enum_expression_in_synpred80_JPA22083);
		enum_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred80_JPA2

	// $ANTLR start synpred81_JPA2
	public final void synpred81_JPA2_fragment() throws RecognitionException {
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:236:7: ( datetime_expression )
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:236:7: datetime_expression
		{
		pushFollow(FOLLOW_datetime_expression_in_synpred81_JPA22091);
		datetime_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred81_JPA2

	// $ANTLR start synpred82_JPA2
	public final void synpred82_JPA2_fragment() throws RecognitionException {
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:237:7: ( boolean_expression )
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:237:7: boolean_expression
		{
		pushFollow(FOLLOW_boolean_expression_in_synpred82_JPA22099);
		boolean_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred82_JPA2

	// $ANTLR start synpred83_JPA2
	public final void synpred83_JPA2_fragment() throws RecognitionException {
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:238:7: ( case_expression )
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:238:7: case_expression
		{
		pushFollow(FOLLOW_case_expression_in_synpred83_JPA22107);
		case_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred83_JPA2

	// $ANTLR start synpred86_JPA2
	public final void synpred86_JPA2_fragment() throws RecognitionException {
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:245:8: ( 'NOT' )
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:245:8: 'NOT'
		{
		match(input,109,FOLLOW_109_in_synpred86_JPA22167); if (state.failed) return;

		}

	}
	// $ANTLR end synpred86_JPA2

	// $ANTLR start synpred87_JPA2
	public final void synpred87_JPA2_fragment() throws RecognitionException {
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:247:7: ( simple_cond_expression )
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:247:7: simple_cond_expression
		{
		pushFollow(FOLLOW_simple_cond_expression_in_synpred87_JPA22182);
		simple_cond_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred87_JPA2

	// $ANTLR start synpred88_JPA2
	public final void synpred88_JPA2_fragment() throws RecognitionException {
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:251:7: ( comparison_expression )
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:251:7: comparison_expression
		{
		pushFollow(FOLLOW_comparison_expression_in_synpred88_JPA22219);
		comparison_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred88_JPA2

	// $ANTLR start synpred89_JPA2
	public final void synpred89_JPA2_fragment() throws RecognitionException {
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:252:7: ( between_expression )
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:252:7: between_expression
		{
		pushFollow(FOLLOW_between_expression_in_synpred89_JPA22227);
		between_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred89_JPA2

	// $ANTLR start synpred90_JPA2
	public final void synpred90_JPA2_fragment() throws RecognitionException {
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:253:7: ( in_expression )
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:253:7: in_expression
		{
		pushFollow(FOLLOW_in_expression_in_synpred90_JPA22235);
		in_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred90_JPA2

	// $ANTLR start synpred91_JPA2
	public final void synpred91_JPA2_fragment() throws RecognitionException {
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:254:7: ( like_expression )
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:254:7: like_expression
		{
		pushFollow(FOLLOW_like_expression_in_synpred91_JPA22243);
		like_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred91_JPA2

	// $ANTLR start synpred92_JPA2
	public final void synpred92_JPA2_fragment() throws RecognitionException {
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:255:7: ( null_comparison_expression )
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:255:7: null_comparison_expression
		{
		pushFollow(FOLLOW_null_comparison_expression_in_synpred92_JPA22251);
		null_comparison_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred92_JPA2

	// $ANTLR start synpred93_JPA2
	public final void synpred93_JPA2_fragment() throws RecognitionException {
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:256:7: ( empty_collection_comparison_expression )
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:256:7: empty_collection_comparison_expression
		{
		pushFollow(FOLLOW_empty_collection_comparison_expression_in_synpred93_JPA22259);
		empty_collection_comparison_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred93_JPA2

	// $ANTLR start synpred94_JPA2
	public final void synpred94_JPA2_fragment() throws RecognitionException {
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:257:7: ( collection_member_expression )
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:257:7: collection_member_expression
		{
		pushFollow(FOLLOW_collection_member_expression_in_synpred94_JPA22267);
		collection_member_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred94_JPA2

	// $ANTLR start synpred113_JPA2
	public final void synpred113_JPA2_fragment() throws RecognitionException {
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:286:7: ( arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression )
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:286:7: arithmetic_expression ( 'NOT' )? 'BETWEEN' arithmetic_expression 'AND' arithmetic_expression
		{
		pushFollow(FOLLOW_arithmetic_expression_in_synpred113_JPA22520);
		arithmetic_expression();
		state._fsp--;
		if (state.failed) return;

		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:286:29: ( 'NOT' )?
		int alt139=2;
		int LA139_0 = input.LA(1);
		if ( (LA139_0==109) ) {
			alt139=1;
		}
		switch (alt139) {
			case 1 :
				// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:286:30: 'NOT'
				{
				match(input,109,FOLLOW_109_in_synpred113_JPA22523); if (state.failed) return;

				}
				break;

		}

		match(input,76,FOLLOW_76_in_synpred113_JPA22527); if (state.failed) return;

		pushFollow(FOLLOW_arithmetic_expression_in_synpred113_JPA22529);
		arithmetic_expression();
		state._fsp--;
		if (state.failed) return;

		match(input,AND,FOLLOW_AND_in_synpred113_JPA22531); if (state.failed) return;

		pushFollow(FOLLOW_arithmetic_expression_in_synpred113_JPA22533);
		arithmetic_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred113_JPA2

	// $ANTLR start synpred115_JPA2
	public final void synpred115_JPA2_fragment() throws RecognitionException {
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:287:7: ( string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression )
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:287:7: string_expression ( 'NOT' )? 'BETWEEN' string_expression 'AND' string_expression
		{
		pushFollow(FOLLOW_string_expression_in_synpred115_JPA22541);
		string_expression();
		state._fsp--;
		if (state.failed) return;

		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:287:25: ( 'NOT' )?
		int alt140=2;
		int LA140_0 = input.LA(1);
		if ( (LA140_0==109) ) {
			alt140=1;
		}
		switch (alt140) {
			case 1 :
				// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:287:26: 'NOT'
				{
				match(input,109,FOLLOW_109_in_synpred115_JPA22544); if (state.failed) return;

				}
				break;

		}

		match(input,76,FOLLOW_76_in_synpred115_JPA22548); if (state.failed) return;

		pushFollow(FOLLOW_string_expression_in_synpred115_JPA22550);
		string_expression();
		state._fsp--;
		if (state.failed) return;

		match(input,AND,FOLLOW_AND_in_synpred115_JPA22552); if (state.failed) return;

		pushFollow(FOLLOW_string_expression_in_synpred115_JPA22554);
		string_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred115_JPA2

	// $ANTLR start synpred132_JPA2
	public final void synpred132_JPA2_fragment() throws RecognitionException {
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:308:7: ( identification_variable )
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:308:7: identification_variable
		{
		pushFollow(FOLLOW_identification_variable_in_synpred132_JPA22809);
		identification_variable();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred132_JPA2

	// $ANTLR start synpred138_JPA2
	public final void synpred138_JPA2_fragment() throws RecognitionException {
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:316:7: ( string_expression comparison_operator ( string_expression | all_or_any_expression ) )
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:316:7: string_expression comparison_operator ( string_expression | all_or_any_expression )
		{
		pushFollow(FOLLOW_string_expression_in_synpred138_JPA22878);
		string_expression();
		state._fsp--;
		if (state.failed) return;

		pushFollow(FOLLOW_comparison_operator_in_synpred138_JPA22880);
		comparison_operator();
		state._fsp--;
		if (state.failed) return;

		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:316:45: ( string_expression | all_or_any_expression )
		int alt142=2;
		int LA142_0 = input.LA(1);
		if ( (LA142_0==AVG||LA142_0==COUNT||(LA142_0 >= LPAREN && LA142_0 <= NAMED_PARAMETER)||(LA142_0 >= STRING_LITERAL && LA142_0 <= SUM)||LA142_0==WORD||LA142_0==52||LA142_0==66||(LA142_0 >= 78 && LA142_0 <= 80)||LA142_0==93||LA142_0==103||LA142_0==112||LA142_0==122||LA142_0==126||LA142_0==129) ) {
			alt142=1;
		}
		else if ( ((LA142_0 >= 73 && LA142_0 <= 74)||LA142_0==120) ) {
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
				// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:316:46: string_expression
				{
				pushFollow(FOLLOW_string_expression_in_synpred138_JPA22883);
				string_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;
			case 2 :
				// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:316:66: all_or_any_expression
				{
				pushFollow(FOLLOW_all_or_any_expression_in_synpred138_JPA22887);
				all_or_any_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;

		}

		}

	}
	// $ANTLR end synpred138_JPA2

	// $ANTLR start synpred141_JPA2
	public final void synpred141_JPA2_fragment() throws RecognitionException {
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:317:7: ( boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression ) )
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:317:7: boolean_expression ( '=' | '<>' ) ( boolean_expression | all_or_any_expression )
		{
		pushFollow(FOLLOW_boolean_expression_in_synpred141_JPA22896);
		boolean_expression();
		state._fsp--;
		if (state.failed) return;

		if ( (input.LA(1) >= 62 && input.LA(1) <= 63) ) {
			input.consume();
			state.errorRecovery=false;
			state.failed=false;
		}
		else {
			if (state.backtracking>0) {state.failed=true; return;}
			MismatchedSetException mse = new MismatchedSetException(null,input);
			throw mse;
		}
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:317:39: ( boolean_expression | all_or_any_expression )
		int alt143=2;
		int LA143_0 = input.LA(1);
		if ( (LA143_0==LPAREN||LA143_0==NAMED_PARAMETER||LA143_0==WORD||LA143_0==52||LA143_0==66||(LA143_0 >= 78 && LA143_0 <= 79)||LA143_0==93||LA143_0==112||(LA143_0 >= 134 && LA143_0 <= 135)) ) {
			alt143=1;
		}
		else if ( ((LA143_0 >= 73 && LA143_0 <= 74)||LA143_0==120) ) {
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
				// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:317:40: boolean_expression
				{
				pushFollow(FOLLOW_boolean_expression_in_synpred141_JPA22907);
				boolean_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;
			case 2 :
				// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:317:61: all_or_any_expression
				{
				pushFollow(FOLLOW_all_or_any_expression_in_synpred141_JPA22911);
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
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:318:7: ( enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression ) )
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:318:7: enum_expression ( '=' | '<>' ) ( enum_expression | all_or_any_expression )
		{
		pushFollow(FOLLOW_enum_expression_in_synpred144_JPA22920);
		enum_expression();
		state._fsp--;
		if (state.failed) return;

		if ( (input.LA(1) >= 62 && input.LA(1) <= 63) ) {
			input.consume();
			state.errorRecovery=false;
			state.failed=false;
		}
		else {
			if (state.backtracking>0) {state.failed=true; return;}
			MismatchedSetException mse = new MismatchedSetException(null,input);
			throw mse;
		}
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:318:34: ( enum_expression | all_or_any_expression )
		int alt144=2;
		int LA144_0 = input.LA(1);
		if ( (LA144_0==LPAREN||LA144_0==NAMED_PARAMETER||LA144_0==WORD||LA144_0==52||LA144_0==66||(LA144_0 >= 78 && LA144_0 <= 79)||LA144_0==112) ) {
			alt144=1;
		}
		else if ( ((LA144_0 >= 73 && LA144_0 <= 74)||LA144_0==120) ) {
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
				// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:318:35: enum_expression
				{
				pushFollow(FOLLOW_enum_expression_in_synpred144_JPA22929);
				enum_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;
			case 2 :
				// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:318:53: all_or_any_expression
				{
				pushFollow(FOLLOW_all_or_any_expression_in_synpred144_JPA22933);
				all_or_any_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;

		}

		}

	}
	// $ANTLR end synpred144_JPA2

	// $ANTLR start synpred146_JPA2
	public final void synpred146_JPA2_fragment() throws RecognitionException {
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:319:7: ( datetime_expression comparison_operator ( datetime_expression | all_or_any_expression ) )
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:319:7: datetime_expression comparison_operator ( datetime_expression | all_or_any_expression )
		{
		pushFollow(FOLLOW_datetime_expression_in_synpred146_JPA22942);
		datetime_expression();
		state._fsp--;
		if (state.failed) return;

		pushFollow(FOLLOW_comparison_operator_in_synpred146_JPA22944);
		comparison_operator();
		state._fsp--;
		if (state.failed) return;

		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:319:47: ( datetime_expression | all_or_any_expression )
		int alt145=2;
		int LA145_0 = input.LA(1);
		if ( (LA145_0==AVG||LA145_0==COUNT||(LA145_0 >= LPAREN && LA145_0 <= NAMED_PARAMETER)||LA145_0==SUM||LA145_0==WORD||LA145_0==52||LA145_0==66||(LA145_0 >= 78 && LA145_0 <= 79)||(LA145_0 >= 81 && LA145_0 <= 83)||LA145_0==93||LA145_0==112) ) {
			alt145=1;
		}
		else if ( ((LA145_0 >= 73 && LA145_0 <= 74)||LA145_0==120) ) {
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
				// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:319:48: datetime_expression
				{
				pushFollow(FOLLOW_datetime_expression_in_synpred146_JPA22947);
				datetime_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;
			case 2 :
				// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:319:70: all_or_any_expression
				{
				pushFollow(FOLLOW_all_or_any_expression_in_synpred146_JPA22951);
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
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:320:7: ( entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression ) )
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:320:7: entity_expression ( '=' | '<>' ) ( entity_expression | all_or_any_expression )
		{
		pushFollow(FOLLOW_entity_expression_in_synpred149_JPA22960);
		entity_expression();
		state._fsp--;
		if (state.failed) return;

		if ( (input.LA(1) >= 62 && input.LA(1) <= 63) ) {
			input.consume();
			state.errorRecovery=false;
			state.failed=false;
		}
		else {
			if (state.backtracking>0) {state.failed=true; return;}
			MismatchedSetException mse = new MismatchedSetException(null,input);
			throw mse;
		}
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:320:38: ( entity_expression | all_or_any_expression )
		int alt146=2;
		int LA146_0 = input.LA(1);
		if ( (LA146_0==NAMED_PARAMETER||LA146_0==WORD||LA146_0==52||LA146_0==66) ) {
			alt146=1;
		}
		else if ( ((LA146_0 >= 73 && LA146_0 <= 74)||LA146_0==120) ) {
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
				// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:320:39: entity_expression
				{
				pushFollow(FOLLOW_entity_expression_in_synpred149_JPA22971);
				entity_expression();
				state._fsp--;
				if (state.failed) return;

				}
				break;
			case 2 :
				// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:320:59: all_or_any_expression
				{
				pushFollow(FOLLOW_all_or_any_expression_in_synpred149_JPA22975);
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
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:321:7: ( entity_type_expression ( '=' | '<>' ) entity_type_expression )
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:321:7: entity_type_expression ( '=' | '<>' ) entity_type_expression
		{
		pushFollow(FOLLOW_entity_type_expression_in_synpred151_JPA22984);
		entity_type_expression();
		state._fsp--;
		if (state.failed) return;

		if ( (input.LA(1) >= 62 && input.LA(1) <= 63) ) {
			input.consume();
			state.errorRecovery=false;
			state.failed=false;
		}
		else {
			if (state.backtracking>0) {state.failed=true; return;}
			MismatchedSetException mse = new MismatchedSetException(null,input);
			throw mse;
		}
		pushFollow(FOLLOW_entity_type_expression_in_synpred151_JPA22994);
		entity_type_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred151_JPA2

	// $ANTLR start synpred158_JPA2
	public final void synpred158_JPA2_fragment() throws RecognitionException {
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:332:7: ( arithmetic_term )
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:332:7: arithmetic_term
		{
		pushFollow(FOLLOW_arithmetic_term_in_synpred158_JPA23075);
		arithmetic_term();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred158_JPA2

	// $ANTLR start synpred160_JPA2
	public final void synpred160_JPA2_fragment() throws RecognitionException {
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:335:7: ( arithmetic_factor )
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:335:7: arithmetic_factor
		{
		pushFollow(FOLLOW_arithmetic_factor_in_synpred160_JPA23104);
		arithmetic_factor();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred160_JPA2

	// $ANTLR start synpred166_JPA2
	public final void synpred166_JPA2_fragment() throws RecognitionException {
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:342:7: ( '(' arithmetic_expression ')' )
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:342:7: '(' arithmetic_expression ')'
		{
		match(input,LPAREN,FOLLOW_LPAREN_in_synpred166_JPA23173); if (state.failed) return;

		pushFollow(FOLLOW_arithmetic_expression_in_synpred166_JPA23174);
		arithmetic_expression();
		state._fsp--;
		if (state.failed) return;

		match(input,RPAREN,FOLLOW_RPAREN_in_synpred166_JPA23175); if (state.failed) return;

		}

	}
	// $ANTLR end synpred166_JPA2

	// $ANTLR start synpred169_JPA2
	public final void synpred169_JPA2_fragment() throws RecognitionException {
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:345:7: ( aggregate_expression )
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:345:7: aggregate_expression
		{
		pushFollow(FOLLOW_aggregate_expression_in_synpred169_JPA23199);
		aggregate_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred169_JPA2

	// $ANTLR start synpred171_JPA2
	public final void synpred171_JPA2_fragment() throws RecognitionException {
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:347:7: ( function_invocation )
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:347:7: function_invocation
		{
		pushFollow(FOLLOW_function_invocation_in_synpred171_JPA23215);
		function_invocation();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred171_JPA2

	// $ANTLR start synpred176_JPA2
	public final void synpred176_JPA2_fragment() throws RecognitionException {
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:354:7: ( aggregate_expression )
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:354:7: aggregate_expression
		{
		pushFollow(FOLLOW_aggregate_expression_in_synpred176_JPA23266);
		aggregate_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred176_JPA2

	// $ANTLR start synpred178_JPA2
	public final void synpred178_JPA2_fragment() throws RecognitionException {
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:356:7: ( function_invocation )
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:356:7: function_invocation
		{
		pushFollow(FOLLOW_function_invocation_in_synpred178_JPA23282);
		function_invocation();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred178_JPA2

	// $ANTLR start synpred179_JPA2
	public final void synpred179_JPA2_fragment() throws RecognitionException {
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:359:7: ( path_expression )
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:359:7: path_expression
		{
		pushFollow(FOLLOW_path_expression_in_synpred179_JPA23301);
		path_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred179_JPA2

	// $ANTLR start synpred182_JPA2
	public final void synpred182_JPA2_fragment() throws RecognitionException {
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:362:7: ( aggregate_expression )
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:362:7: aggregate_expression
		{
		pushFollow(FOLLOW_aggregate_expression_in_synpred182_JPA23325);
		aggregate_expression();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred182_JPA2

	// $ANTLR start synpred184_JPA2
	public final void synpred184_JPA2_fragment() throws RecognitionException {
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:364:7: ( function_invocation )
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:364:7: function_invocation
		{
		pushFollow(FOLLOW_function_invocation_in_synpred184_JPA23341);
		function_invocation();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred184_JPA2

	// $ANTLR start synpred185_JPA2
	public final void synpred185_JPA2_fragment() throws RecognitionException {
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:365:7: ( date_time_timestamp_literal )
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:365:7: date_time_timestamp_literal
		{
		pushFollow(FOLLOW_date_time_timestamp_literal_in_synpred185_JPA23349);
		date_time_timestamp_literal();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred185_JPA2

	// $ANTLR start synpred222_JPA2
	public final void synpred222_JPA2_fragment() throws RecognitionException {
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:415:7: ( literal )
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:415:7: literal
		{
		pushFollow(FOLLOW_literal_in_synpred222_JPA23791);
		literal();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred222_JPA2

	// $ANTLR start synpred224_JPA2
	public final void synpred224_JPA2_fragment() throws RecognitionException {
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:417:7: ( input_parameter )
		// F:\\WORK\\platform\\project-all\\project-all-trunk\\cuba\\modules\\global\\src\\com\\haulmont\\cuba\\core\\sys\\jpql\\antlr2\\JPA2.g:417:7: input_parameter
		{
		pushFollow(FOLLOW_input_parameter_in_synpred224_JPA23807);
		input_parameter();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred224_JPA2

	// Delegated rules

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
	public final boolean synpred222_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred222_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred224_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred224_JPA2_fragment(); // can never throw exception
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
	public final boolean synpred31_JPA2() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred31_JPA2_fragment(); // can never throw exception
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


	protected DFA41 dfa41 = new DFA41(this);
	protected DFA47 dfa47 = new DFA47(this);
	static final String DFA41_eotS =
		"\14\uffff";
	static final String DFA41_eofS =
		"\14\uffff";
	static final String DFA41_minS =
		"\1\6\1\25\2\uffff\1\13\1\62\1\34\1\uffff\1\16\1\34\1\16\1\0";
	static final String DFA41_maxS =
		"\1\135\1\25\2\uffff\2\62\1\71\1\uffff\1\62\1\71\1\62\1\0";
	static final String DFA41_acceptS =
		"\2\uffff\1\1\1\3\3\uffff\1\2\4\uffff";
	static final String DFA41_specialS =
		"\13\uffff\1\0}>";
	static final String[] DFA41_transitionS = {
			"\1\2\2\uffff\1\1\14\uffff\2\2\7\uffff\1\2\75\uffff\1\3",
			"\1\4",
			"",
			"",
			"\1\5\46\uffff\1\6",
			"\1\6",
			"\1\7\34\uffff\1\10",
			"",
			"\1\11\43\uffff\1\11",
			"\1\13\34\uffff\1\12",
			"\1\11\43\uffff\1\11",
			"\1\uffff"
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
			return "168:1: aggregate_expression : ( aggregate_expression_function_name '(' ( DISTINCT )? path_expression ')' -> ^( T_AGGREGATE_EXPR[] aggregate_expression_function_name '(' ( 'DISTINCT' )? path_expression ')' ) | 'COUNT' '(' ( DISTINCT )? count_argument ')' -> ^( T_AGGREGATE_EXPR[] 'COUNT' '(' ( 'DISTINCT' )? count_argument ')' ) | function_invocation );";
		}
		@Override
		public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
			TokenStream input = (TokenStream)_input;
			int _s = s;
			switch ( s ) {
					case 0 : 
						int LA41_11 = input.LA(1);
						 
						int index41_11 = input.index();
						input.rewind();
						s = -1;
						if ( (synpred48_JPA2()) ) {s = 2;}
						else if ( (synpred50_JPA2()) ) {s = 7;}
						 
						input.seek(index41_11);
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

	static final String DFA47_eotS =
		"\15\uffff";
	static final String DFA47_eofS =
		"\1\uffff\1\5\7\uffff\3\5\1\uffff";
	static final String DFA47_minS =
		"\1\62\1\5\2\62\1\16\2\uffff\2\34\3\5\1\16";
	static final String DFA47_maxS =
		"\1\u0082\1\71\3\62\2\uffff\2\34\1\71\2\67\1\62";
	static final String DFA47_acceptS =
		"\5\uffff\1\1\1\2\6\uffff";
	static final String DFA47_specialS =
		"\15\uffff}>";
	static final String[] DFA47_transitionS = {
			"\1\1\57\uffff\1\2\37\uffff\1\3",
			"\1\5\4\uffff\1\6\54\uffff\1\5\1\uffff\1\4",
			"\1\7",
			"\1\10",
			"\1\11\43\uffff\1\11",
			"",
			"",
			"\1\12",
			"\1\13",
			"\1\5\4\uffff\1\6\54\uffff\1\5\1\uffff\1\14",
			"\1\5\4\uffff\1\6\54\uffff\1\5",
			"\1\5\4\uffff\1\6\54\uffff\1\5",
			"\1\11\43\uffff\1\11"
	};

	static final short[] DFA47_eot = DFA.unpackEncodedString(DFA47_eotS);
	static final short[] DFA47_eof = DFA.unpackEncodedString(DFA47_eofS);
	static final char[] DFA47_min = DFA.unpackEncodedStringToUnsignedChars(DFA47_minS);
	static final char[] DFA47_max = DFA.unpackEncodedStringToUnsignedChars(DFA47_maxS);
	static final short[] DFA47_accept = DFA.unpackEncodedString(DFA47_acceptS);
	static final short[] DFA47_special = DFA.unpackEncodedString(DFA47_specialS);
	static final short[][] DFA47_transition;

	static {
		int numStates = DFA47_transitionS.length;
		DFA47_transition = new short[numStates][];
		for (int i=0; i<numStates; i++) {
			DFA47_transition[i] = DFA.unpackEncodedString(DFA47_transitionS[i]);
		}
	}

	protected class DFA47 extends DFA {

		public DFA47(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 47;
			this.eot = DFA47_eot;
			this.eof = DFA47_eof;
			this.min = DFA47_min;
			this.max = DFA47_max;
			this.accept = DFA47_accept;
			this.special = DFA47_special;
			this.transition = DFA47_transition;
		}
		@Override
		public String getDescription() {
			return "190:1: orderby_item : ( orderby_variable ( 'ASC' )? -> ^( T_ORDER_BY_FIELD[] orderby_variable ( 'ASC' )? ) | orderby_variable 'DESC' -> ^( T_ORDER_BY_FIELD[] orderby_variable 'DESC' ) );";
		}
	}

	public static final BitSet FOLLOW_select_statement_in_ql_statement416 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_117_in_select_statement431 = new BitSet(new long[]{0x09540000C1E20A40L,0xC68314D1200FC104L,0x00000000000000C2L});
	public static final BitSet FOLLOW_select_clause_in_select_statement433 = new BitSet(new long[]{0x0000000000000000L,0x0000000010000000L});
	public static final BitSet FOLLOW_from_clause_in_select_statement435 = new BitSet(new long[]{0x000000000400C002L,0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_where_clause_in_select_statement438 = new BitSet(new long[]{0x000000000400C002L});
	public static final BitSet FOLLOW_groupby_clause_in_select_statement443 = new BitSet(new long[]{0x0000000004008002L});
	public static final BitSet FOLLOW_having_clause_in_select_statement448 = new BitSet(new long[]{0x0000000004000002L});
	public static final BitSet FOLLOW_orderby_clause_in_select_statement453 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_128_in_update_statement509 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_update_clause_in_update_statement511 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_where_clause_in_update_statement514 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_85_in_delete_statement527 = new BitSet(new long[]{0x0000000000000000L,0x0000000010000000L});
	public static final BitSet FOLLOW_92_in_delete_statement529 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_delete_clause_in_delete_statement531 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_where_clause_in_delete_statement534 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_92_in_from_clause551 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_identification_variable_declaration_in_from_clause553 = new BitSet(new long[]{0x0080000000000002L});
	public static final BitSet FOLLOW_55_in_from_clause556 = new BitSet(new long[]{0x0004000000000000L,0x0000000080000000L});
	public static final BitSet FOLLOW_identification_variable_declaration_or_collection_member_declaration_in_from_clause558 = new BitSet(new long[]{0x0080000000000002L});
	public static final BitSet FOLLOW_identification_variable_declaration_in_identification_variable_declaration_or_collection_member_declaration592 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_collection_member_declaration_in_identification_variable_declaration_or_collection_member_declaration601 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_range_variable_declaration_in_identification_variable_declaration625 = new BitSet(new long[]{0x00000000000D0002L});
	public static final BitSet FOLLOW_joined_clause_in_identification_variable_declaration627 = new BitSet(new long[]{0x00000000000D0002L});
	public static final BitSet FOLLOW_join_in_joined_clause654 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_fetch_join_in_joined_clause658 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_name_in_range_variable_declaration670 = new BitSet(new long[]{0x0004000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_75_in_range_variable_declaration673 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_identification_variable_in_range_variable_declaration677 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_join_spec_in_join706 = new BitSet(new long[]{0x0004000000000000L,0x2000000000000000L});
	public static final BitSet FOLLOW_join_association_path_expression_in_join708 = new BitSet(new long[]{0x0004000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_75_in_join711 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_identification_variable_in_join715 = new BitSet(new long[]{0x0000000000000002L,0x0008000000000000L});
	public static final BitSet FOLLOW_join_condition_in_join718 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_join_spec_in_fetch_join749 = new BitSet(new long[]{0x0000000000002000L});
	public static final BitSet FOLLOW_FETCH_in_fetch_join751 = new BitSet(new long[]{0x0004000000000000L,0x2000000000000000L});
	public static final BitSet FOLLOW_join_association_path_expression_in_fetch_join753 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LEFT_in_join_spec767 = new BitSet(new long[]{0x0000000008040000L});
	public static final BitSet FOLLOW_OUTER_in_join_spec771 = new BitSet(new long[]{0x0000000000040000L});
	public static final BitSet FOLLOW_INNER_in_join_spec777 = new BitSet(new long[]{0x0000000000040000L});
	public static final BitSet FOLLOW_JOIN_in_join_spec782 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_115_in_join_condition794 = new BitSet(new long[]{0x09540000C1E20240L,0xC68124D1280FC1FCL,0x00000000000000C2L});
	public static final BitSet FOLLOW_conditional_expression_in_join_condition796 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_join_association_path_expression810 = new BitSet(new long[]{0x0200000000000000L});
	public static final BitSet FOLLOW_57_in_join_association_path_expression812 = new BitSet(new long[]{0x0004000000004000L});
	public static final BitSet FOLLOW_field_in_join_association_path_expression815 = new BitSet(new long[]{0x0200000000000000L});
	public static final BitSet FOLLOW_57_in_join_association_path_expression816 = new BitSet(new long[]{0x0004000000004000L});
	public static final BitSet FOLLOW_field_in_join_association_path_expression820 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_125_in_join_association_path_expression854 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_identification_variable_in_join_association_path_expression856 = new BitSet(new long[]{0x0200000000000000L});
	public static final BitSet FOLLOW_57_in_join_association_path_expression858 = new BitSet(new long[]{0x0004000000004000L,0x0000000000000800L});
	public static final BitSet FOLLOW_field_in_join_association_path_expression861 = new BitSet(new long[]{0x0200000000000000L});
	public static final BitSet FOLLOW_57_in_join_association_path_expression862 = new BitSet(new long[]{0x0004000000004000L,0x0000000000000800L});
	public static final BitSet FOLLOW_field_in_join_association_path_expression866 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_75_in_join_association_path_expression869 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_subtype_in_join_association_path_expression871 = new BitSet(new long[]{0x0000000010000000L});
	public static final BitSet FOLLOW_RPAREN_in_join_association_path_expression873 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_95_in_collection_member_declaration910 = new BitSet(new long[]{0x0000000000200000L});
	public static final BitSet FOLLOW_LPAREN_in_collection_member_declaration911 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_path_expression_in_collection_member_declaration913 = new BitSet(new long[]{0x0000000010000000L});
	public static final BitSet FOLLOW_RPAREN_in_collection_member_declaration915 = new BitSet(new long[]{0x0004000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_75_in_collection_member_declaration918 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_identification_variable_in_collection_member_declaration922 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_map_field_identification_variable_in_qualified_identification_variable951 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_89_in_qualified_identification_variable959 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_identification_variable_in_qualified_identification_variable960 = new BitSet(new long[]{0x0000000010000000L});
	public static final BitSet FOLLOW_RPAREN_in_qualified_identification_variable961 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_98_in_map_field_identification_variable968 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_identification_variable_in_map_field_identification_variable969 = new BitSet(new long[]{0x0000000010000000L});
	public static final BitSet FOLLOW_RPAREN_in_map_field_identification_variable970 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_130_in_map_field_identification_variable974 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_identification_variable_in_map_field_identification_variable975 = new BitSet(new long[]{0x0000000010000000L});
	public static final BitSet FOLLOW_RPAREN_in_map_field_identification_variable976 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_path_expression990 = new BitSet(new long[]{0x0200000000000000L});
	public static final BitSet FOLLOW_57_in_path_expression992 = new BitSet(new long[]{0x0004000000004000L});
	public static final BitSet FOLLOW_field_in_path_expression995 = new BitSet(new long[]{0x0200000000000000L});
	public static final BitSet FOLLOW_57_in_path_expression996 = new BitSet(new long[]{0x0004000000004000L});
	public static final BitSet FOLLOW_field_in_path_expression1000 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_general_identification_variable1038 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_map_field_identification_variable_in_general_identification_variable1046 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_name_in_update_clause1057 = new BitSet(new long[]{0x0004000000000000L,0x0040000000000800L});
	public static final BitSet FOLLOW_75_in_update_clause1061 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_identification_variable_in_update_clause1065 = new BitSet(new long[]{0x0000000000000000L,0x0040000000000000L});
	public static final BitSet FOLLOW_118_in_update_clause1069 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_update_item_in_update_clause1071 = new BitSet(new long[]{0x0080000000000002L});
	public static final BitSet FOLLOW_55_in_update_clause1074 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_update_item_in_update_clause1076 = new BitSet(new long[]{0x0080000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_update_item1090 = new BitSet(new long[]{0x0200000000000000L});
	public static final BitSet FOLLOW_57_in_update_item1091 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_single_valued_embeddable_object_field_in_update_item1094 = new BitSet(new long[]{0x0200000000000000L});
	public static final BitSet FOLLOW_57_in_update_item1095 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_single_valued_object_field_in_update_item1098 = new BitSet(new long[]{0x8000000000000000L});
	public static final BitSet FOLLOW_63_in_update_item1100 = new BitSet(new long[]{0x09540000C1E20240L,0xC68184D1200FC104L,0x00000000000000C2L});
	public static final BitSet FOLLOW_new_value_in_update_item1102 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_new_value1113 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_entity_expression_in_new_value1121 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_111_in_new_value1129 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_name_in_delete_clause1140 = new BitSet(new long[]{0x0004000000000002L,0x0000000000000800L});
	public static final BitSet FOLLOW_75_in_delete_clause1144 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_identification_variable_in_delete_clause1148 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_DISTINCT_in_select_clause1162 = new BitSet(new long[]{0x09540000C1E20240L,0xC68314D1200FC104L,0x00000000000000C2L});
	public static final BitSet FOLLOW_select_item_in_select_clause1166 = new BitSet(new long[]{0x0080000000000002L});
	public static final BitSet FOLLOW_55_in_select_clause1169 = new BitSet(new long[]{0x09540000C1E20240L,0xC68314D1200FC104L,0x00000000000000C2L});
	public static final BitSet FOLLOW_select_item_in_select_clause1171 = new BitSet(new long[]{0x0080000000000002L});
	public static final BitSet FOLLOW_select_expression_in_select_item1210 = new BitSet(new long[]{0x0004000000000002L,0x0000000000000800L});
	public static final BitSet FOLLOW_75_in_select_item1214 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_result_variable_in_select_item1218 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_select_expression1231 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_select_expression1239 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_select_expression1257 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_select_expression1265 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_113_in_select_expression1273 = new BitSet(new long[]{0x0000000000200000L});
	public static final BitSet FOLLOW_LPAREN_in_select_expression1275 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_identification_variable_in_select_expression1276 = new BitSet(new long[]{0x0000000010000000L});
	public static final BitSet FOLLOW_RPAREN_in_select_expression1277 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_constructor_expression_in_select_expression1285 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_108_in_constructor_expression1296 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_constructor_name_in_constructor_expression1298 = new BitSet(new long[]{0x0000000000200000L});
	public static final BitSet FOLLOW_LPAREN_in_constructor_expression1300 = new BitSet(new long[]{0x09540000C1E20240L,0xC68104D1200FC104L,0x00000000000000C2L});
	public static final BitSet FOLLOW_constructor_item_in_constructor_expression1302 = new BitSet(new long[]{0x0080000010000000L});
	public static final BitSet FOLLOW_55_in_constructor_expression1305 = new BitSet(new long[]{0x09540000C1E20240L,0xC68104D1200FC104L,0x00000000000000C2L});
	public static final BitSet FOLLOW_constructor_item_in_constructor_expression1307 = new BitSet(new long[]{0x0080000010000000L});
	public static final BitSet FOLLOW_RPAREN_in_constructor_expression1311 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_constructor_item1322 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_constructor_item1330 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_constructor_item1338 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_constructor_item1346 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_function_name_in_aggregate_expression1357 = new BitSet(new long[]{0x0000000000200000L});
	public static final BitSet FOLLOW_LPAREN_in_aggregate_expression1359 = new BitSet(new long[]{0x0004000000000800L});
	public static final BitSet FOLLOW_DISTINCT_in_aggregate_expression1361 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_path_expression_in_aggregate_expression1365 = new BitSet(new long[]{0x0000000010000000L});
	public static final BitSet FOLLOW_RPAREN_in_aggregate_expression1366 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_COUNT_in_aggregate_expression1400 = new BitSet(new long[]{0x0000000000200000L});
	public static final BitSet FOLLOW_LPAREN_in_aggregate_expression1402 = new BitSet(new long[]{0x0004000000000800L});
	public static final BitSet FOLLOW_DISTINCT_in_aggregate_expression1404 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_count_argument_in_aggregate_expression1408 = new BitSet(new long[]{0x0000000010000000L});
	public static final BitSet FOLLOW_RPAREN_in_aggregate_expression1410 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_aggregate_expression1445 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_count_argument1482 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_count_argument1486 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_132_in_where_clause1499 = new BitSet(new long[]{0x09540000C1E20240L,0xC68124D1280FC1FCL,0x00000000000000C2L});
	public static final BitSet FOLLOW_conditional_expression_in_where_clause1501 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_GROUP_in_groupby_clause1523 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_BY_in_groupby_clause1525 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_groupby_item_in_groupby_clause1527 = new BitSet(new long[]{0x0080000000000002L});
	public static final BitSet FOLLOW_55_in_groupby_clause1530 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_groupby_item_in_groupby_clause1532 = new BitSet(new long[]{0x0080000000000002L});
	public static final BitSet FOLLOW_path_expression_in_groupby_item1566 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_groupby_item1570 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_HAVING_in_having_clause1581 = new BitSet(new long[]{0x09540000C1E20240L,0xC68124D1280FC1FCL,0x00000000000000C2L});
	public static final BitSet FOLLOW_conditional_expression_in_having_clause1583 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ORDER_in_orderby_clause1594 = new BitSet(new long[]{0x0000000000000080L});
	public static final BitSet FOLLOW_BY_in_orderby_clause1596 = new BitSet(new long[]{0x0004000000000000L,0x0000000400000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_orderby_item_in_orderby_clause1598 = new BitSet(new long[]{0x0080000000000002L});
	public static final BitSet FOLLOW_55_in_orderby_clause1601 = new BitSet(new long[]{0x0004000000000000L,0x0000000400000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_orderby_item_in_orderby_clause1603 = new BitSet(new long[]{0x0080000000000002L});
	public static final BitSet FOLLOW_orderby_variable_in_orderby_item1637 = new BitSet(new long[]{0x0000000000000022L});
	public static final BitSet FOLLOW_ASC_in_orderby_item1640 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_orderby_variable_in_orderby_item1672 = new BitSet(new long[]{0x0000000000000400L});
	public static final BitSet FOLLOW_DESC_in_orderby_item1675 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_orderby_variable1704 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_general_identification_variable_in_orderby_variable1708 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_result_variable_in_orderby_variable1712 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_subquery1726 = new BitSet(new long[]{0x0000000000000000L,0x0020000000000000L});
	public static final BitSet FOLLOW_117_in_subquery1728 = new BitSet(new long[]{0x09540000C1E20A40L,0xC68104D1200FC104L,0x00000000000000C2L});
	public static final BitSet FOLLOW_simple_select_clause_in_subquery1730 = new BitSet(new long[]{0x0000000000000000L,0x0000000010000000L});
	public static final BitSet FOLLOW_subquery_from_clause_in_subquery1732 = new BitSet(new long[]{0x000000001000C000L,0x0000000000000000L,0x0000000000000010L});
	public static final BitSet FOLLOW_where_clause_in_subquery1735 = new BitSet(new long[]{0x000000001000C000L});
	public static final BitSet FOLLOW_groupby_clause_in_subquery1740 = new BitSet(new long[]{0x0000000010008000L});
	public static final BitSet FOLLOW_having_clause_in_subquery1745 = new BitSet(new long[]{0x0000000010000000L});
	public static final BitSet FOLLOW_RPAREN_in_subquery1751 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_92_in_subquery_from_clause1801 = new BitSet(new long[]{0x0004000000000000L,0x2000000080000000L});
	public static final BitSet FOLLOW_subselect_identification_variable_declaration_in_subquery_from_clause1803 = new BitSet(new long[]{0x0080000000000002L});
	public static final BitSet FOLLOW_55_in_subquery_from_clause1806 = new BitSet(new long[]{0x0004000000000000L,0x2000000080000000L});
	public static final BitSet FOLLOW_subselect_identification_variable_declaration_in_subquery_from_clause1808 = new BitSet(new long[]{0x0080000000000002L});
	public static final BitSet FOLLOW_identification_variable_declaration_in_subselect_identification_variable_declaration1846 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_derived_path_expression_in_subselect_identification_variable_declaration1854 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_75_in_subselect_identification_variable_declaration1856 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_identification_variable_in_subselect_identification_variable_declaration1858 = new BitSet(new long[]{0x00000000000D0002L});
	public static final BitSet FOLLOW_join_in_subselect_identification_variable_declaration1861 = new BitSet(new long[]{0x00000000000D0002L});
	public static final BitSet FOLLOW_derived_collection_member_declaration_in_subselect_identification_variable_declaration1871 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_general_derived_path_in_derived_path_expression1882 = new BitSet(new long[]{0x0200000000000000L});
	public static final BitSet FOLLOW_57_in_derived_path_expression1883 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_single_valued_object_field_in_derived_path_expression1884 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_general_derived_path_in_derived_path_expression1892 = new BitSet(new long[]{0x0200000000000000L});
	public static final BitSet FOLLOW_57_in_derived_path_expression1893 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_collection_valued_field_in_derived_path_expression1894 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_derived_path_in_general_derived_path1905 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_treated_derived_path_in_general_derived_path1913 = new BitSet(new long[]{0x0200000000000002L});
	public static final BitSet FOLLOW_57_in_general_derived_path1915 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_single_valued_object_field_in_general_derived_path1916 = new BitSet(new long[]{0x0200000000000002L});
	public static final BitSet FOLLOW_superquery_identification_variable_in_simple_derived_path1934 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_125_in_treated_derived_path1951 = new BitSet(new long[]{0x0004000000000000L,0x2000000000000000L});
	public static final BitSet FOLLOW_general_derived_path_in_treated_derived_path1952 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
	public static final BitSet FOLLOW_75_in_treated_derived_path1954 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_subtype_in_treated_derived_path1956 = new BitSet(new long[]{0x0000000010000000L});
	public static final BitSet FOLLOW_RPAREN_in_treated_derived_path1958 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_95_in_derived_collection_member_declaration1969 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_superquery_identification_variable_in_derived_collection_member_declaration1971 = new BitSet(new long[]{0x0200000000000000L});
	public static final BitSet FOLLOW_57_in_derived_collection_member_declaration1972 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_single_valued_object_field_in_derived_collection_member_declaration1974 = new BitSet(new long[]{0x0200000000000000L});
	public static final BitSet FOLLOW_57_in_derived_collection_member_declaration1976 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_collection_valued_field_in_derived_collection_member_declaration1979 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_DISTINCT_in_simple_select_clause1992 = new BitSet(new long[]{0x09540000C1E20240L,0xC68104D1200FC104L,0x00000000000000C2L});
	public static final BitSet FOLLOW_simple_select_expression_in_simple_select_clause1996 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_simple_select_expression2032 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_simple_select_expression2040 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_simple_select_expression2048 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_simple_select_expression2056 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_expression_in_scalar_expression2067 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_scalar_expression2075 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_enum_expression_in_scalar_expression2083 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_datetime_expression_in_scalar_expression2091 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_boolean_expression_in_scalar_expression2099 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_scalar_expression2107 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_type_expression_in_scalar_expression2115 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_conditional_term_in_conditional_expression2127 = new BitSet(new long[]{0x0000000002000002L});
	public static final BitSet FOLLOW_OR_in_conditional_expression2131 = new BitSet(new long[]{0x09540000C1E20240L,0xC68124D1280FC1FCL,0x00000000000000C2L});
	public static final BitSet FOLLOW_conditional_term_in_conditional_expression2133 = new BitSet(new long[]{0x0000000002000002L});
	public static final BitSet FOLLOW_conditional_factor_in_conditional_term2147 = new BitSet(new long[]{0x0000000000000012L});
	public static final BitSet FOLLOW_AND_in_conditional_term2151 = new BitSet(new long[]{0x09540000C1E20240L,0xC68124D1280FC1FCL,0x00000000000000C2L});
	public static final BitSet FOLLOW_conditional_factor_in_conditional_term2153 = new BitSet(new long[]{0x0000000000000012L});
	public static final BitSet FOLLOW_109_in_conditional_factor2167 = new BitSet(new long[]{0x09540000C1E20240L,0xC68124D1280FC1FCL,0x00000000000000C2L});
	public static final BitSet FOLLOW_conditional_primary_in_conditional_factor2171 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_cond_expression_in_conditional_primary2182 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_conditional_primary2206 = new BitSet(new long[]{0x09540000C1E20240L,0xC68124D1280FC1FCL,0x00000000000000C2L});
	public static final BitSet FOLLOW_conditional_expression_in_conditional_primary2207 = new BitSet(new long[]{0x0000000010000000L});
	public static final BitSet FOLLOW_RPAREN_in_conditional_primary2208 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_comparison_expression_in_simple_cond_expression2219 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_between_expression_in_simple_cond_expression2227 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_in_expression_in_simple_cond_expression2235 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_like_expression_in_simple_cond_expression2243 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_null_comparison_expression_in_simple_cond_expression2251 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_empty_collection_comparison_expression_in_simple_cond_expression2259 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_collection_member_expression_in_simple_cond_expression2267 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_exists_expression_in_simple_cond_expression2275 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_macro_expression_in_simple_cond_expression2283 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_between_macro_expression_in_date_macro_expression2296 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_before_macro_expression_in_date_macro_expression2304 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_after_macro_expression_in_date_macro_expression2312 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_equals_macro_expression_in_date_macro_expression2320 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_today_macro_expression_in_date_macro_expression2328 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_67_in_date_between_macro_expression2340 = new BitSet(new long[]{0x0000000000200000L});
	public static final BitSet FOLLOW_LPAREN_in_date_between_macro_expression2342 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_path_expression_in_date_between_macro_expression2344 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_55_in_date_between_macro_expression2346 = new BitSet(new long[]{0x0000000000000000L,0x0000400000000000L});
	public static final BitSet FOLLOW_110_in_date_between_macro_expression2348 = new BitSet(new long[]{0x01C0000000000000L});
	public static final BitSet FOLLOW_set_in_date_between_macro_expression2351 = new BitSet(new long[]{0x0800000000020000L});
	public static final BitSet FOLLOW_numeric_literal_in_date_between_macro_expression2359 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_55_in_date_between_macro_expression2363 = new BitSet(new long[]{0x0000000000000000L,0x0000400000000000L});
	public static final BitSet FOLLOW_110_in_date_between_macro_expression2365 = new BitSet(new long[]{0x01C0000000000000L});
	public static final BitSet FOLLOW_set_in_date_between_macro_expression2368 = new BitSet(new long[]{0x0800000000020000L});
	public static final BitSet FOLLOW_numeric_literal_in_date_between_macro_expression2376 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_55_in_date_between_macro_expression2380 = new BitSet(new long[]{0x0000000000000000L,0x00100A0040100000L,0x0000000000000020L});
	public static final BitSet FOLLOW_set_in_date_between_macro_expression2382 = new BitSet(new long[]{0x0000000010000000L});
	public static final BitSet FOLLOW_RPAREN_in_date_between_macro_expression2405 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_69_in_date_before_macro_expression2417 = new BitSet(new long[]{0x0000000000200000L});
	public static final BitSet FOLLOW_LPAREN_in_date_before_macro_expression2419 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_path_expression_in_date_before_macro_expression2421 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_55_in_date_before_macro_expression2423 = new BitSet(new long[]{0x0014000001000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_path_expression_in_date_before_macro_expression2426 = new BitSet(new long[]{0x0000000010000000L});
	public static final BitSet FOLLOW_input_parameter_in_date_before_macro_expression2430 = new BitSet(new long[]{0x0000000010000000L});
	public static final BitSet FOLLOW_RPAREN_in_date_before_macro_expression2433 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_68_in_date_after_macro_expression2445 = new BitSet(new long[]{0x0000000000200000L});
	public static final BitSet FOLLOW_LPAREN_in_date_after_macro_expression2447 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_path_expression_in_date_after_macro_expression2449 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_55_in_date_after_macro_expression2451 = new BitSet(new long[]{0x0014000001000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_path_expression_in_date_after_macro_expression2454 = new BitSet(new long[]{0x0000000010000000L});
	public static final BitSet FOLLOW_input_parameter_in_date_after_macro_expression2458 = new BitSet(new long[]{0x0000000010000000L});
	public static final BitSet FOLLOW_RPAREN_in_date_after_macro_expression2461 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_70_in_date_equals_macro_expression2473 = new BitSet(new long[]{0x0000000000200000L});
	public static final BitSet FOLLOW_LPAREN_in_date_equals_macro_expression2475 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_path_expression_in_date_equals_macro_expression2477 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_55_in_date_equals_macro_expression2479 = new BitSet(new long[]{0x0014000001000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_path_expression_in_date_equals_macro_expression2482 = new BitSet(new long[]{0x0000000010000000L});
	public static final BitSet FOLLOW_input_parameter_in_date_equals_macro_expression2486 = new BitSet(new long[]{0x0000000010000000L});
	public static final BitSet FOLLOW_RPAREN_in_date_equals_macro_expression2489 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_71_in_date_today_macro_expression2501 = new BitSet(new long[]{0x0000000000200000L});
	public static final BitSet FOLLOW_LPAREN_in_date_today_macro_expression2503 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_path_expression_in_date_today_macro_expression2505 = new BitSet(new long[]{0x0000000010000000L});
	public static final BitSet FOLLOW_RPAREN_in_date_today_macro_expression2507 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_expression_in_between_expression2520 = new BitSet(new long[]{0x0000000000000000L,0x0000200000001000L});
	public static final BitSet FOLLOW_109_in_between_expression2523 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
	public static final BitSet FOLLOW_76_in_between_expression2527 = new BitSet(new long[]{0x0954000081E20240L,0x028104512000C104L});
	public static final BitSet FOLLOW_arithmetic_expression_in_between_expression2529 = new BitSet(new long[]{0x0000000000000010L});
	public static final BitSet FOLLOW_AND_in_between_expression2531 = new BitSet(new long[]{0x0954000081E20240L,0x028104512000C104L});
	public static final BitSet FOLLOW_arithmetic_expression_in_between_expression2533 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_between_expression2541 = new BitSet(new long[]{0x0000000000000000L,0x0000200000001000L});
	public static final BitSet FOLLOW_109_in_between_expression2544 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
	public static final BitSet FOLLOW_76_in_between_expression2548 = new BitSet(new long[]{0x00140000C1E00240L,0x440100802001C004L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_between_expression2550 = new BitSet(new long[]{0x0000000000000010L});
	public static final BitSet FOLLOW_AND_in_between_expression2552 = new BitSet(new long[]{0x00140000C1E00240L,0x440100802001C004L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_between_expression2554 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_datetime_expression_in_between_expression2562 = new BitSet(new long[]{0x0000000000000000L,0x0000200000001000L});
	public static final BitSet FOLLOW_109_in_between_expression2565 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
	public static final BitSet FOLLOW_76_in_between_expression2569 = new BitSet(new long[]{0x0014000081E00240L,0x00010000200EC004L});
	public static final BitSet FOLLOW_datetime_expression_in_between_expression2571 = new BitSet(new long[]{0x0000000000000010L});
	public static final BitSet FOLLOW_AND_in_between_expression2573 = new BitSet(new long[]{0x0014000081E00240L,0x00010000200EC004L});
	public static final BitSet FOLLOW_datetime_expression_in_between_expression2575 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_in_expression2587 = new BitSet(new long[]{0x0000000000000000L,0x0000200080000000L});
	public static final BitSet FOLLOW_type_discriminator_in_in_expression2591 = new BitSet(new long[]{0x0000000000000000L,0x0000200080000000L});
	public static final BitSet FOLLOW_109_in_in_expression2595 = new BitSet(new long[]{0x0000000000000000L,0x0000000080000000L});
	public static final BitSet FOLLOW_95_in_in_expression2599 = new BitSet(new long[]{0x0010000001200000L,0x0000000000000004L});
	public static final BitSet FOLLOW_LPAREN_in_in_expression2615 = new BitSet(new long[]{0x0014000001000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_in_item_in_in_expression2617 = new BitSet(new long[]{0x0080000010000000L});
	public static final BitSet FOLLOW_55_in_in_expression2620 = new BitSet(new long[]{0x0014000001000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_in_item_in_in_expression2622 = new BitSet(new long[]{0x0080000010000000L});
	public static final BitSet FOLLOW_RPAREN_in_in_expression2626 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_in_expression2642 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_collection_valued_input_parameter_in_in_expression2658 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_literal_in_in_item2671 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_single_valued_input_parameter_in_in_item2675 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_like_expression2686 = new BitSet(new long[]{0x0000000000000000L,0x0000202000000000L});
	public static final BitSet FOLLOW_109_in_like_expression2689 = new BitSet(new long[]{0x0000000000000000L,0x0000002000000000L});
	public static final BitSet FOLLOW_101_in_like_expression2693 = new BitSet(new long[]{0x0010000041000000L,0x0000000000000004L});
	public static final BitSet FOLLOW_pattern_value_in_like_expression2696 = new BitSet(new long[]{0x0000000000000002L,0x0000000004000000L});
	public static final BitSet FOLLOW_input_parameter_in_like_expression2700 = new BitSet(new long[]{0x0000000000000002L,0x0000000004000000L});
	public static final BitSet FOLLOW_90_in_like_expression2703 = new BitSet(new long[]{0x0000000000001000L});
	public static final BitSet FOLLOW_escape_character_in_like_expression2705 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_null_comparison_expression2719 = new BitSet(new long[]{0x0000000000000000L,0x0000000200000000L});
	public static final BitSet FOLLOW_input_parameter_in_null_comparison_expression2723 = new BitSet(new long[]{0x0000000000000000L,0x0000000200000000L});
	public static final BitSet FOLLOW_97_in_null_comparison_expression2726 = new BitSet(new long[]{0x0000000000000000L,0x0000A00000000000L});
	public static final BitSet FOLLOW_109_in_null_comparison_expression2729 = new BitSet(new long[]{0x0000000000000000L,0x0000800000000000L});
	public static final BitSet FOLLOW_111_in_null_comparison_expression2733 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_empty_collection_comparison_expression2744 = new BitSet(new long[]{0x0000000000000000L,0x0000000200000000L});
	public static final BitSet FOLLOW_97_in_empty_collection_comparison_expression2746 = new BitSet(new long[]{0x0000000000000000L,0x0000200000800000L});
	public static final BitSet FOLLOW_109_in_empty_collection_comparison_expression2749 = new BitSet(new long[]{0x0000000000000000L,0x0000000000800000L});
	public static final BitSet FOLLOW_87_in_empty_collection_comparison_expression2753 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_or_value_expression_in_collection_member_expression2764 = new BitSet(new long[]{0x0000000000000000L,0x0000210000000000L});
	public static final BitSet FOLLOW_109_in_collection_member_expression2768 = new BitSet(new long[]{0x0000000000000000L,0x0000010000000000L});
	public static final BitSet FOLLOW_104_in_collection_member_expression2772 = new BitSet(new long[]{0x0004000000000000L,0x0004000000000000L});
	public static final BitSet FOLLOW_114_in_collection_member_expression2775 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_path_expression_in_collection_member_expression2779 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_entity_or_value_expression2790 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_entity_or_value_expression_in_entity_or_value_expression2798 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_simple_entity_or_value_expression2809 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_simple_entity_or_value_expression2817 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_literal_in_simple_entity_or_value_expression2825 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_109_in_exists_expression2837 = new BitSet(new long[]{0x0000000000000000L,0x0000000008000000L});
	public static final BitSet FOLLOW_91_in_exists_expression2841 = new BitSet(new long[]{0x0000000000200000L});
	public static final BitSet FOLLOW_subquery_in_exists_expression2843 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_set_in_all_or_any_expression2854 = new BitSet(new long[]{0x0000000000200000L});
	public static final BitSet FOLLOW_subquery_in_all_or_any_expression2867 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_comparison_expression2878 = new BitSet(new long[]{0xF000000000000000L,0x0000000000000003L});
	public static final BitSet FOLLOW_comparison_operator_in_comparison_expression2880 = new BitSet(new long[]{0x00140000C1E00240L,0x450100802001C604L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_comparison_expression2883 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression2887 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_boolean_expression_in_comparison_expression2896 = new BitSet(new long[]{0xC000000000000000L});
	public static final BitSet FOLLOW_set_in_comparison_expression2898 = new BitSet(new long[]{0x0014000001200000L,0x010100002000C604L,0x00000000000000C0L});
	public static final BitSet FOLLOW_boolean_expression_in_comparison_expression2907 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression2911 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_enum_expression_in_comparison_expression2920 = new BitSet(new long[]{0xC000000000000000L});
	public static final BitSet FOLLOW_set_in_comparison_expression2922 = new BitSet(new long[]{0x0014000001200000L,0x010100000000C604L});
	public static final BitSet FOLLOW_enum_expression_in_comparison_expression2929 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression2933 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_datetime_expression_in_comparison_expression2942 = new BitSet(new long[]{0xF000000000000000L,0x0000000000000003L});
	public static final BitSet FOLLOW_comparison_operator_in_comparison_expression2944 = new BitSet(new long[]{0x0014000081E00240L,0x01010000200EC604L});
	public static final BitSet FOLLOW_datetime_expression_in_comparison_expression2947 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression2951 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_expression_in_comparison_expression2960 = new BitSet(new long[]{0xC000000000000000L});
	public static final BitSet FOLLOW_set_in_comparison_expression2962 = new BitSet(new long[]{0x0014000001000000L,0x0100000000000604L});
	public static final BitSet FOLLOW_entity_expression_in_comparison_expression2971 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression2975 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_type_expression_in_comparison_expression2984 = new BitSet(new long[]{0xC000000000000000L});
	public static final BitSet FOLLOW_set_in_comparison_expression2986 = new BitSet(new long[]{0x0014000001000000L,0x8000000000000004L});
	public static final BitSet FOLLOW_entity_type_expression_in_comparison_expression2994 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_expression_in_comparison_expression3002 = new BitSet(new long[]{0xF000000000000000L,0x0000000000000003L});
	public static final BitSet FOLLOW_comparison_operator_in_comparison_expression3004 = new BitSet(new long[]{0x0954000081E20240L,0x038104512000C704L});
	public static final BitSet FOLLOW_arithmetic_expression_in_comparison_expression3007 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_comparison_expression3011 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_term_in_arithmetic_expression3075 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_term_in_arithmetic_expression3083 = new BitSet(new long[]{0x0140000000000000L});
	public static final BitSet FOLLOW_set_in_arithmetic_expression3085 = new BitSet(new long[]{0x0954000081E20240L,0x028104512000C104L});
	public static final BitSet FOLLOW_arithmetic_term_in_arithmetic_expression3093 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_factor_in_arithmetic_term3104 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_factor_in_arithmetic_term3112 = new BitSet(new long[]{0x0420000000000000L});
	public static final BitSet FOLLOW_set_in_arithmetic_term3114 = new BitSet(new long[]{0x0954000081E20240L,0x028104512000C104L});
	public static final BitSet FOLLOW_arithmetic_factor_in_arithmetic_term3123 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_primary_in_arithmetic_factor3146 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_arithmetic_primary3157 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_numeric_literal_in_arithmetic_primary3165 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_arithmetic_primary3173 = new BitSet(new long[]{0x0954000081E20240L,0x028104512000C104L});
	public static final BitSet FOLLOW_arithmetic_expression_in_arithmetic_primary3174 = new BitSet(new long[]{0x0000000010000000L});
	public static final BitSet FOLLOW_RPAREN_in_arithmetic_primary3175 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_arithmetic_primary3183 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_functions_returning_numerics_in_arithmetic_primary3191 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_arithmetic_primary3199 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_arithmetic_primary3207 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_arithmetic_primary3215 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_arithmetic_primary3223 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_string_expression3234 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_literal_in_string_expression3242 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_string_expression3250 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_functions_returning_strings_in_string_expression3258 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_string_expression3266 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_string_expression3274 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_string_expression3282 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_string_expression3290 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_datetime_expression3301 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_datetime_expression3309 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_functions_returning_datetime_in_datetime_expression3317 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_datetime_expression3325 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_datetime_expression3333 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_datetime_expression3341 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_time_timestamp_literal_in_datetime_expression3349 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_datetime_expression3357 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_boolean_expression3368 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_boolean_literal_in_boolean_expression3376 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_boolean_expression3384 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_boolean_expression3392 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_boolean_expression3400 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_boolean_expression3408 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_enum_expression3419 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_enum_literal_in_enum_expression3427 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_enum_expression3435 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_enum_expression3443 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_subquery_in_enum_expression3451 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_entity_expression3462 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_entity_expression_in_entity_expression3470 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_simple_entity_expression3481 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_simple_entity_expression3489 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_type_discriminator_in_entity_type_expression3500 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_type_literal_in_entity_type_expression3508 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_entity_type_expression3516 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_127_in_type_discriminator3527 = new BitSet(new long[]{0x0014000001000000L,0x0000000400000004L,0x0000000000000004L});
	public static final BitSet FOLLOW_general_identification_variable_in_type_discriminator3529 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_type_discriminator3533 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_type_discriminator3537 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_100_in_functions_returning_numerics3549 = new BitSet(new long[]{0x00140000C1E00240L,0x440100802001C004L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_numerics3550 = new BitSet(new long[]{0x0000000010000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3551 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_102_in_functions_returning_numerics3559 = new BitSet(new long[]{0x00140000C1E00240L,0x440100802001C004L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_numerics3561 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_55_in_functions_returning_numerics3562 = new BitSet(new long[]{0x00140000C1E00240L,0x440100802001C004L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_numerics3564 = new BitSet(new long[]{0x0080000010000000L});
	public static final BitSet FOLLOW_55_in_functions_returning_numerics3566 = new BitSet(new long[]{0x0954000081E20240L,0x028104512000C104L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_numerics3567 = new BitSet(new long[]{0x0000000010000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3570 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_72_in_functions_returning_numerics3578 = new BitSet(new long[]{0x0954000081E20240L,0x028104512000C104L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_numerics3579 = new BitSet(new long[]{0x0000000010000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3580 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_121_in_functions_returning_numerics3588 = new BitSet(new long[]{0x0954000081E20240L,0x028104512000C104L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_numerics3589 = new BitSet(new long[]{0x0000000010000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3590 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_106_in_functions_returning_numerics3598 = new BitSet(new long[]{0x0954000081E20240L,0x028104512000C104L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_numerics3599 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_55_in_functions_returning_numerics3600 = new BitSet(new long[]{0x0954000081E20240L,0x028104512000C104L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_numerics3602 = new BitSet(new long[]{0x0000000010000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3603 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_119_in_functions_returning_numerics3611 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_path_expression_in_functions_returning_numerics3612 = new BitSet(new long[]{0x0000000010000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3613 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_96_in_functions_returning_numerics3621 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_identification_variable_in_functions_returning_numerics3622 = new BitSet(new long[]{0x0000000010000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_numerics3623 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_80_in_functions_returning_strings3661 = new BitSet(new long[]{0x00140000C1E00240L,0x440100802001C004L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings3662 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_55_in_functions_returning_strings3663 = new BitSet(new long[]{0x00140000C1E00240L,0x440100802001C004L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings3665 = new BitSet(new long[]{0x0080000010000000L});
	public static final BitSet FOLLOW_55_in_functions_returning_strings3668 = new BitSet(new long[]{0x00140000C1E00240L,0x440100802001C004L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings3670 = new BitSet(new long[]{0x0080000010000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings3673 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_122_in_functions_returning_strings3681 = new BitSet(new long[]{0x00140000C1E00240L,0x440100802001C004L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings3683 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_55_in_functions_returning_strings3684 = new BitSet(new long[]{0x0954000081E20240L,0x028104512000C104L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_strings3686 = new BitSet(new long[]{0x0080000010000000L});
	public static final BitSet FOLLOW_55_in_functions_returning_strings3689 = new BitSet(new long[]{0x0954000081E20240L,0x028104512000C104L});
	public static final BitSet FOLLOW_arithmetic_expression_in_functions_returning_strings3691 = new BitSet(new long[]{0x0000000010000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings3694 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_126_in_functions_returning_strings3702 = new BitSet(new long[]{0x00140001C1E00240L,0x540100883001E004L,0x0000000000000002L});
	public static final BitSet FOLLOW_trim_specification_in_functions_returning_strings3705 = new BitSet(new long[]{0x0000000100000000L,0x0000000010000000L});
	public static final BitSet FOLLOW_trim_character_in_functions_returning_strings3710 = new BitSet(new long[]{0x0000000000000000L,0x0000000010000000L});
	public static final BitSet FOLLOW_92_in_functions_returning_strings3714 = new BitSet(new long[]{0x00140000C1E00240L,0x440100802001C004L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings3718 = new BitSet(new long[]{0x0000000010000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings3720 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_103_in_functions_returning_strings3728 = new BitSet(new long[]{0x00140000C1E00240L,0x440100802001C004L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings3729 = new BitSet(new long[]{0x0000000010000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings3730 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_129_in_functions_returning_strings3738 = new BitSet(new long[]{0x00140000C1E00240L,0x440100802001C004L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_functions_returning_strings3739 = new BitSet(new long[]{0x0000000010000000L});
	public static final BitSet FOLLOW_RPAREN_in_functions_returning_strings3740 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_93_in_function_invocation3770 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_function_name_in_function_invocation3771 = new BitSet(new long[]{0x0080000010000000L});
	public static final BitSet FOLLOW_55_in_function_invocation3774 = new BitSet(new long[]{0x09540000C1E20240L,0xC68104D1200FC104L,0x00000000000000C2L});
	public static final BitSet FOLLOW_function_arg_in_function_invocation3776 = new BitSet(new long[]{0x0080000010000000L});
	public static final BitSet FOLLOW_RPAREN_in_function_invocation3780 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_literal_in_function_arg3791 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_function_arg3799 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_function_arg3807 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_function_arg3815 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_general_case_expression_in_case_expression3826 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_case_expression_in_case_expression3834 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_coalesce_expression_in_case_expression3842 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_nullif_expression_in_case_expression3850 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_78_in_general_case_expression3861 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000008L});
	public static final BitSet FOLLOW_when_clause_in_general_case_expression3863 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L,0x0000000000000008L});
	public static final BitSet FOLLOW_when_clause_in_general_case_expression3866 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L,0x0000000000000008L});
	public static final BitSet FOLLOW_86_in_general_case_expression3870 = new BitSet(new long[]{0x09540000C1E20240L,0xC68104D1200FC104L,0x00000000000000C2L});
	public static final BitSet FOLLOW_scalar_expression_in_general_case_expression3872 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
	public static final BitSet FOLLOW_88_in_general_case_expression3874 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_131_in_when_clause3885 = new BitSet(new long[]{0x09540000C1E20240L,0xC68124D1280FC1FCL,0x00000000000000C2L});
	public static final BitSet FOLLOW_conditional_expression_in_when_clause3887 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L});
	public static final BitSet FOLLOW_123_in_when_clause3889 = new BitSet(new long[]{0x09540000C1E20240L,0xC68104D1200FC104L,0x00000000000000C2L});
	public static final BitSet FOLLOW_scalar_expression_in_when_clause3891 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_78_in_simple_case_expression3902 = new BitSet(new long[]{0x0004000000000000L,0x8000000000000000L});
	public static final BitSet FOLLOW_case_operand_in_simple_case_expression3904 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000008L});
	public static final BitSet FOLLOW_simple_when_clause_in_simple_case_expression3906 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L,0x0000000000000008L});
	public static final BitSet FOLLOW_simple_when_clause_in_simple_case_expression3909 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L,0x0000000000000008L});
	public static final BitSet FOLLOW_86_in_simple_case_expression3913 = new BitSet(new long[]{0x09540000C1E20240L,0xC68104D1200FC104L,0x00000000000000C2L});
	public static final BitSet FOLLOW_scalar_expression_in_simple_case_expression3915 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
	public static final BitSet FOLLOW_88_in_simple_case_expression3917 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_case_operand3928 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_type_discriminator_in_case_operand3936 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_131_in_simple_when_clause3947 = new BitSet(new long[]{0x09540000C1E20240L,0xC68104D1200FC104L,0x00000000000000C2L});
	public static final BitSet FOLLOW_scalar_expression_in_simple_when_clause3949 = new BitSet(new long[]{0x0000000000000000L,0x0800000000000000L});
	public static final BitSet FOLLOW_123_in_simple_when_clause3951 = new BitSet(new long[]{0x09540000C1E20240L,0xC68104D1200FC104L,0x00000000000000C2L});
	public static final BitSet FOLLOW_scalar_expression_in_simple_when_clause3953 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_79_in_coalesce_expression3964 = new BitSet(new long[]{0x09540000C1E20240L,0xC68104D1200FC104L,0x00000000000000C2L});
	public static final BitSet FOLLOW_scalar_expression_in_coalesce_expression3965 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_55_in_coalesce_expression3968 = new BitSet(new long[]{0x09540000C1E20240L,0xC68104D1200FC104L,0x00000000000000C2L});
	public static final BitSet FOLLOW_scalar_expression_in_coalesce_expression3970 = new BitSet(new long[]{0x0080000010000000L});
	public static final BitSet FOLLOW_RPAREN_in_coalesce_expression3973 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_112_in_nullif_expression3984 = new BitSet(new long[]{0x09540000C1E20240L,0xC68104D1200FC104L,0x00000000000000C2L});
	public static final BitSet FOLLOW_scalar_expression_in_nullif_expression3985 = new BitSet(new long[]{0x0080000000000000L});
	public static final BitSet FOLLOW_55_in_nullif_expression3987 = new BitSet(new long[]{0x09540000C1E20240L,0xC68104D1200FC104L,0x00000000000000C2L});
	public static final BitSet FOLLOW_scalar_expression_in_nullif_expression3989 = new BitSet(new long[]{0x0000000010000000L});
	public static final BitSet FOLLOW_RPAREN_in_nullif_expression3990 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_66_in_input_parameter4003 = new BitSet(new long[]{0x0800000000020000L});
	public static final BitSet FOLLOW_numeric_literal_in_input_parameter4005 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_NAMED_PARAMETER_in_input_parameter4028 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_52_in_input_parameter4049 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_WORD_in_input_parameter4051 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000000L,0x0000000000000100L});
	public static final BitSet FOLLOW_136_in_input_parameter4053 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_literal4081 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_constructor_name4093 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_enum_literal4105 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_identification_variable4154 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_parameter_name4166 = new BitSet(new long[]{0x0200000000000002L});
	public static final BitSet FOLLOW_57_in_parameter_name4169 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_WORD_in_parameter_name4172 = new BitSet(new long[]{0x0200000000000002L});
	public static final BitSet FOLLOW_ESCAPE_CHARACTER_in_escape_character4186 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TRIM_CHARACTER_in_trim_character4197 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_STRING_LITERAL_in_string_literal4208 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_59_in_numeric_literal4220 = new BitSet(new long[]{0x0000000000020000L});
	public static final BitSet FOLLOW_INT_NUMERAL_in_numeric_literal4224 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_single_valued_object_field4236 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_single_valued_embeddable_object_field4247 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_collection_valued_field4258 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_entity_name4269 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_subtype4280 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_entity_type_literal4291 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_function_name4302 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_state_field4313 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_result_variable4324 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_superquery_identification_variable4335 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WORD_in_date_time_timestamp_literal4346 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_literal_in_pattern_value4357 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_collection_valued_input_parameter4368 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_single_valued_input_parameter4379 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_synpred30_JPA21113 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_entity_expression_in_synpred31_JPA21121 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_synpred38_JPA21231 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_synpred39_JPA21239 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_synpred40_JPA21257 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_synpred41_JPA21265 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_synpred44_JPA21322 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_synpred45_JPA21330 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_synpred46_JPA21338 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_function_name_in_synpred48_JPA21357 = new BitSet(new long[]{0x0000000000200000L});
	public static final BitSet FOLLOW_LPAREN_in_synpred48_JPA21359 = new BitSet(new long[]{0x0004000000000800L});
	public static final BitSet FOLLOW_DISTINCT_in_synpred48_JPA21361 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_path_expression_in_synpred48_JPA21365 = new BitSet(new long[]{0x0000000010000000L});
	public static final BitSet FOLLOW_RPAREN_in_synpred48_JPA21366 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_COUNT_in_synpred50_JPA21400 = new BitSet(new long[]{0x0000000000200000L});
	public static final BitSet FOLLOW_LPAREN_in_synpred50_JPA21402 = new BitSet(new long[]{0x0004000000000800L});
	public static final BitSet FOLLOW_DISTINCT_in_synpred50_JPA21404 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_count_argument_in_synpred50_JPA21408 = new BitSet(new long[]{0x0000000010000000L});
	public static final BitSet FOLLOW_RPAREN_in_synpred50_JPA21410 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_general_identification_variable_in_synpred62_JPA21708 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_general_derived_path_in_synpred70_JPA21882 = new BitSet(new long[]{0x0200000000000000L});
	public static final BitSet FOLLOW_57_in_synpred70_JPA21883 = new BitSet(new long[]{0x0004000000000000L});
	public static final BitSet FOLLOW_single_valued_object_field_in_synpred70_JPA21884 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_synpred75_JPA22032 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_scalar_expression_in_synpred76_JPA22040 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_synpred77_JPA22048 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_expression_in_synpred78_JPA22067 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_synpred79_JPA22075 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_enum_expression_in_synpred80_JPA22083 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_datetime_expression_in_synpred81_JPA22091 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_boolean_expression_in_synpred82_JPA22099 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_case_expression_in_synpred83_JPA22107 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_109_in_synpred86_JPA22167 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_simple_cond_expression_in_synpred87_JPA22182 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_comparison_expression_in_synpred88_JPA22219 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_between_expression_in_synpred89_JPA22227 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_in_expression_in_synpred90_JPA22235 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_like_expression_in_synpred91_JPA22243 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_null_comparison_expression_in_synpred92_JPA22251 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_empty_collection_comparison_expression_in_synpred93_JPA22259 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_collection_member_expression_in_synpred94_JPA22267 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_expression_in_synpred113_JPA22520 = new BitSet(new long[]{0x0000000000000000L,0x0000200000001000L});
	public static final BitSet FOLLOW_109_in_synpred113_JPA22523 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
	public static final BitSet FOLLOW_76_in_synpred113_JPA22527 = new BitSet(new long[]{0x0954000081E20240L,0x028104512000C104L});
	public static final BitSet FOLLOW_arithmetic_expression_in_synpred113_JPA22529 = new BitSet(new long[]{0x0000000000000010L});
	public static final BitSet FOLLOW_AND_in_synpred113_JPA22531 = new BitSet(new long[]{0x0954000081E20240L,0x028104512000C104L});
	public static final BitSet FOLLOW_arithmetic_expression_in_synpred113_JPA22533 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_synpred115_JPA22541 = new BitSet(new long[]{0x0000000000000000L,0x0000200000001000L});
	public static final BitSet FOLLOW_109_in_synpred115_JPA22544 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
	public static final BitSet FOLLOW_76_in_synpred115_JPA22548 = new BitSet(new long[]{0x00140000C1E00240L,0x440100802001C004L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_synpred115_JPA22550 = new BitSet(new long[]{0x0000000000000010L});
	public static final BitSet FOLLOW_AND_in_synpred115_JPA22552 = new BitSet(new long[]{0x00140000C1E00240L,0x440100802001C004L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_synpred115_JPA22554 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identification_variable_in_synpred132_JPA22809 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_synpred138_JPA22878 = new BitSet(new long[]{0xF000000000000000L,0x0000000000000003L});
	public static final BitSet FOLLOW_comparison_operator_in_synpred138_JPA22880 = new BitSet(new long[]{0x00140000C1E00240L,0x450100802001C604L,0x0000000000000002L});
	public static final BitSet FOLLOW_string_expression_in_synpred138_JPA22883 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_synpred138_JPA22887 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_boolean_expression_in_synpred141_JPA22896 = new BitSet(new long[]{0xC000000000000000L});
	public static final BitSet FOLLOW_set_in_synpred141_JPA22898 = new BitSet(new long[]{0x0014000001200000L,0x010100002000C604L,0x00000000000000C0L});
	public static final BitSet FOLLOW_boolean_expression_in_synpred141_JPA22907 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_synpred141_JPA22911 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_enum_expression_in_synpred144_JPA22920 = new BitSet(new long[]{0xC000000000000000L});
	public static final BitSet FOLLOW_set_in_synpred144_JPA22922 = new BitSet(new long[]{0x0014000001200000L,0x010100000000C604L});
	public static final BitSet FOLLOW_enum_expression_in_synpred144_JPA22929 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_synpred144_JPA22933 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_datetime_expression_in_synpred146_JPA22942 = new BitSet(new long[]{0xF000000000000000L,0x0000000000000003L});
	public static final BitSet FOLLOW_comparison_operator_in_synpred146_JPA22944 = new BitSet(new long[]{0x0014000081E00240L,0x01010000200EC604L});
	public static final BitSet FOLLOW_datetime_expression_in_synpred146_JPA22947 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_synpred146_JPA22951 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_expression_in_synpred149_JPA22960 = new BitSet(new long[]{0xC000000000000000L});
	public static final BitSet FOLLOW_set_in_synpred149_JPA22962 = new BitSet(new long[]{0x0014000001000000L,0x0100000000000604L});
	public static final BitSet FOLLOW_entity_expression_in_synpred149_JPA22971 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_all_or_any_expression_in_synpred149_JPA22975 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_entity_type_expression_in_synpred151_JPA22984 = new BitSet(new long[]{0xC000000000000000L});
	public static final BitSet FOLLOW_set_in_synpred151_JPA22986 = new BitSet(new long[]{0x0014000001000000L,0x8000000000000004L});
	public static final BitSet FOLLOW_entity_type_expression_in_synpred151_JPA22994 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_term_in_synpred158_JPA23075 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_arithmetic_factor_in_synpred160_JPA23104 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_synpred166_JPA23173 = new BitSet(new long[]{0x0954000081E20240L,0x028104512000C104L});
	public static final BitSet FOLLOW_arithmetic_expression_in_synpred166_JPA23174 = new BitSet(new long[]{0x0000000010000000L});
	public static final BitSet FOLLOW_RPAREN_in_synpred166_JPA23175 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_synpred169_JPA23199 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_synpred171_JPA23215 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_synpred176_JPA23266 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_synpred178_JPA23282 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_path_expression_in_synpred179_JPA23301 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_aggregate_expression_in_synpred182_JPA23325 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_function_invocation_in_synpred184_JPA23341 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_time_timestamp_literal_in_synpred185_JPA23349 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_literal_in_synpred222_JPA23791 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_input_parameter_in_synpred224_JPA23807 = new BitSet(new long[]{0x0000000000000002L});
}
