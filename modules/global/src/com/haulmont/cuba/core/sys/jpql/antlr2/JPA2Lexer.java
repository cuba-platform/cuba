// $ANTLR 3.5.2 JPA2.g 2021-07-06 17:53:14

package com.haulmont.cuba.core.sys.jpql.antlr2;


import com.haulmont.cuba.core.sys.jpql.JPA2RecognitionException;
import org.antlr.runtime.*;

@SuppressWarnings("all")
public class JPA2Lexer extends Lexer {
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
	// delegators
	public Lexer[] getDelegates() {
		return new Lexer[] {};
	}

	public JPA2Lexer() {} 
	public JPA2Lexer(CharStream input) {
		this(input, new RecognizerSharedState());
	}
	public JPA2Lexer(CharStream input, RecognizerSharedState state) {
		super(input,state);
	}
	@Override public String getGrammarFileName() { return "JPA2.g"; }

	// $ANTLR start "AND"
	public final void mAND() throws RecognitionException {
		try {
			int _type = AND;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:7:5: ( 'AND' )
			// JPA2.g:7:7: 'AND'
			{
			match("AND"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "AND"

	// $ANTLR start "AS"
	public final void mAS() throws RecognitionException {
		try {
			int _type = AS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:8:4: ( 'AS' )
			// JPA2.g:8:6: 'AS'
			{
			match("AS"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "AS"

	// $ANTLR start "ASC"
	public final void mASC() throws RecognitionException {
		try {
			int _type = ASC;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:9:5: ( 'ASC' )
			// JPA2.g:9:7: 'ASC'
			{
			match("ASC"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "ASC"

	// $ANTLR start "AVG"
	public final void mAVG() throws RecognitionException {
		try {
			int _type = AVG;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:10:5: ( 'AVG' )
			// JPA2.g:10:7: 'AVG'
			{
			match("AVG"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "AVG"

	// $ANTLR start "BY"
	public final void mBY() throws RecognitionException {
		try {
			int _type = BY;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:11:4: ( 'BY' )
			// JPA2.g:11:6: 'BY'
			{
			match("BY"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "BY"

	// $ANTLR start "CASE"
	public final void mCASE() throws RecognitionException {
		try {
			int _type = CASE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:12:6: ( 'CASE' )
			// JPA2.g:12:8: 'CASE'
			{
			match("CASE"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "CASE"

	// $ANTLR start "COUNT"
	public final void mCOUNT() throws RecognitionException {
		try {
			int _type = COUNT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:13:7: ( 'COUNT' )
			// JPA2.g:13:9: 'COUNT'
			{
			match("COUNT"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "COUNT"

	// $ANTLR start "DESC"
	public final void mDESC() throws RecognitionException {
		try {
			int _type = DESC;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:14:6: ( 'DESC' )
			// JPA2.g:14:8: 'DESC'
			{
			match("DESC"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "DESC"

	// $ANTLR start "DISTINCT"
	public final void mDISTINCT() throws RecognitionException {
		try {
			int _type = DISTINCT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:15:10: ( 'DISTINCT' )
			// JPA2.g:15:12: 'DISTINCT'
			{
			match("DISTINCT"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "DISTINCT"

	// $ANTLR start "ELSE"
	public final void mELSE() throws RecognitionException {
		try {
			int _type = ELSE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:16:6: ( 'ELSE' )
			// JPA2.g:16:8: 'ELSE'
			{
			match("ELSE"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "ELSE"

	// $ANTLR start "END"
	public final void mEND() throws RecognitionException {
		try {
			int _type = END;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:17:5: ( 'END' )
			// JPA2.g:17:7: 'END'
			{
			match("END"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "END"

	// $ANTLR start "FETCH"
	public final void mFETCH() throws RecognitionException {
		try {
			int _type = FETCH;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:18:7: ( 'FETCH' )
			// JPA2.g:18:9: 'FETCH'
			{
			match("FETCH"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "FETCH"

	// $ANTLR start "GROUP"
	public final void mGROUP() throws RecognitionException {
		try {
			int _type = GROUP;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:19:7: ( 'GROUP' )
			// JPA2.g:19:9: 'GROUP'
			{
			match("GROUP"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "GROUP"

	// $ANTLR start "HAVING"
	public final void mHAVING() throws RecognitionException {
		try {
			int _type = HAVING;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:20:8: ( 'HAVING' )
			// JPA2.g:20:10: 'HAVING'
			{
			match("HAVING"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "HAVING"

	// $ANTLR start "INNER"
	public final void mINNER() throws RecognitionException {
		try {
			int _type = INNER;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:21:7: ( 'INNER' )
			// JPA2.g:21:9: 'INNER'
			{
			match("INNER"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "INNER"

	// $ANTLR start "JOIN"
	public final void mJOIN() throws RecognitionException {
		try {
			int _type = JOIN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:22:6: ( 'JOIN' )
			// JPA2.g:22:8: 'JOIN'
			{
			match("JOIN"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "JOIN"

	// $ANTLR start "LEFT"
	public final void mLEFT() throws RecognitionException {
		try {
			int _type = LEFT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:23:6: ( 'LEFT' )
			// JPA2.g:23:8: 'LEFT'
			{
			match("LEFT"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "LEFT"

	// $ANTLR start "LOWER"
	public final void mLOWER() throws RecognitionException {
		try {
			int _type = LOWER;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:24:7: ( 'LOWER' )
			// JPA2.g:24:9: 'LOWER'
			{
			match("LOWER"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "LOWER"

	// $ANTLR start "LPAREN"
	public final void mLPAREN() throws RecognitionException {
		try {
			int _type = LPAREN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:25:8: ( '(' )
			// JPA2.g:25:10: '('
			{
			match('('); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "LPAREN"

	// $ANTLR start "MAX"
	public final void mMAX() throws RecognitionException {
		try {
			int _type = MAX;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:26:5: ( 'MAX' )
			// JPA2.g:26:7: 'MAX'
			{
			match("MAX"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "MAX"

	// $ANTLR start "MIN"
	public final void mMIN() throws RecognitionException {
		try {
			int _type = MIN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:27:5: ( 'MIN' )
			// JPA2.g:27:7: 'MIN'
			{
			match("MIN"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "MIN"

	// $ANTLR start "OR"
	public final void mOR() throws RecognitionException {
		try {
			int _type = OR;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:28:4: ( 'OR' )
			// JPA2.g:28:6: 'OR'
			{
			match("OR"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "OR"

	// $ANTLR start "ORDER"
	public final void mORDER() throws RecognitionException {
		try {
			int _type = ORDER;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:29:7: ( 'ORDER' )
			// JPA2.g:29:9: 'ORDER'
			{
			match("ORDER"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "ORDER"

	// $ANTLR start "OUTER"
	public final void mOUTER() throws RecognitionException {
		try {
			int _type = OUTER;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:30:7: ( 'OUTER' )
			// JPA2.g:30:9: 'OUTER'
			{
			match("OUTER"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "OUTER"

	// $ANTLR start "RPAREN"
	public final void mRPAREN() throws RecognitionException {
		try {
			int _type = RPAREN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:31:8: ( ')' )
			// JPA2.g:31:10: ')'
			{
			match(')'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "RPAREN"

	// $ANTLR start "SET"
	public final void mSET() throws RecognitionException {
		try {
			int _type = SET;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:32:5: ( 'SET' )
			// JPA2.g:32:7: 'SET'
			{
			match("SET"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "SET"

	// $ANTLR start "SUM"
	public final void mSUM() throws RecognitionException {
		try {
			int _type = SUM;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:33:5: ( 'SUM' )
			// JPA2.g:33:7: 'SUM'
			{
			match("SUM"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "SUM"

	// $ANTLR start "THEN"
	public final void mTHEN() throws RecognitionException {
		try {
			int _type = THEN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:34:6: ( 'THEN' )
			// JPA2.g:34:8: 'THEN'
			{
			match("THEN"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "THEN"

	// $ANTLR start "WHEN"
	public final void mWHEN() throws RecognitionException {
		try {
			int _type = WHEN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:35:6: ( 'WHEN' )
			// JPA2.g:35:8: 'WHEN'
			{
			match("WHEN"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "WHEN"

	// $ANTLR start "T__63"
	public final void mT__63() throws RecognitionException {
		try {
			int _type = T__63;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:36:7: ( '${' )
			// JPA2.g:36:9: '${'
			{
			match("${"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__63"

	// $ANTLR start "T__64"
	public final void mT__64() throws RecognitionException {
		try {
			int _type = T__64;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:37:7: ( '*' )
			// JPA2.g:37:9: '*'
			{
			match('*'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__64"

	// $ANTLR start "T__65"
	public final void mT__65() throws RecognitionException {
		try {
			int _type = T__65;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:38:7: ( '+' )
			// JPA2.g:38:9: '+'
			{
			match('+'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__65"

	// $ANTLR start "T__66"
	public final void mT__66() throws RecognitionException {
		try {
			int _type = T__66;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:39:7: ( ',' )
			// JPA2.g:39:9: ','
			{
			match(','); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__66"

	// $ANTLR start "T__67"
	public final void mT__67() throws RecognitionException {
		try {
			int _type = T__67;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:40:7: ( '-' )
			// JPA2.g:40:9: '-'
			{
			match('-'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__67"

	// $ANTLR start "T__68"
	public final void mT__68() throws RecognitionException {
		try {
			int _type = T__68;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:41:7: ( '.' )
			// JPA2.g:41:9: '.'
			{
			match('.'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__68"

	// $ANTLR start "T__69"
	public final void mT__69() throws RecognitionException {
		try {
			int _type = T__69;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:42:7: ( '/' )
			// JPA2.g:42:9: '/'
			{
			match('/'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__69"

	// $ANTLR start "T__70"
	public final void mT__70() throws RecognitionException {
		try {
			int _type = T__70;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:43:7: ( '0x' )
			// JPA2.g:43:9: '0x'
			{
			match("0x"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__70"

	// $ANTLR start "T__71"
	public final void mT__71() throws RecognitionException {
		try {
			int _type = T__71;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:44:7: ( '<' )
			// JPA2.g:44:9: '<'
			{
			match('<'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__71"

	// $ANTLR start "T__72"
	public final void mT__72() throws RecognitionException {
		try {
			int _type = T__72;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:45:7: ( '<=' )
			// JPA2.g:45:9: '<='
			{
			match("<="); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__72"

	// $ANTLR start "T__73"
	public final void mT__73() throws RecognitionException {
		try {
			int _type = T__73;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:46:7: ( '<>' )
			// JPA2.g:46:9: '<>'
			{
			match("<>"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__73"

	// $ANTLR start "T__74"
	public final void mT__74() throws RecognitionException {
		try {
			int _type = T__74;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:47:7: ( '=' )
			// JPA2.g:47:9: '='
			{
			match('='); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__74"

	// $ANTLR start "T__75"
	public final void mT__75() throws RecognitionException {
		try {
			int _type = T__75;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:48:7: ( '>' )
			// JPA2.g:48:9: '>'
			{
			match('>'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__75"

	// $ANTLR start "T__76"
	public final void mT__76() throws RecognitionException {
		try {
			int _type = T__76;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:49:7: ( '>=' )
			// JPA2.g:49:9: '>='
			{
			match(">="); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__76"

	// $ANTLR start "T__77"
	public final void mT__77() throws RecognitionException {
		try {
			int _type = T__77;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:50:7: ( '?' )
			// JPA2.g:50:9: '?'
			{
			match('?'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__77"

	// $ANTLR start "T__78"
	public final void mT__78() throws RecognitionException {
		try {
			int _type = T__78;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:51:7: ( '@BETWEEN' )
			// JPA2.g:51:9: '@BETWEEN'
			{
			match("@BETWEEN"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__78"

	// $ANTLR start "T__79"
	public final void mT__79() throws RecognitionException {
		try {
			int _type = T__79;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:52:7: ( '@DATEAFTER' )
			// JPA2.g:52:9: '@DATEAFTER'
			{
			match("@DATEAFTER"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__79"

	// $ANTLR start "T__80"
	public final void mT__80() throws RecognitionException {
		try {
			int _type = T__80;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:53:7: ( '@DATEBEFORE' )
			// JPA2.g:53:9: '@DATEBEFORE'
			{
			match("@DATEBEFORE"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__80"

	// $ANTLR start "T__81"
	public final void mT__81() throws RecognitionException {
		try {
			int _type = T__81;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:54:7: ( '@DATEEQUALS' )
			// JPA2.g:54:9: '@DATEEQUALS'
			{
			match("@DATEEQUALS"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__81"

	// $ANTLR start "T__82"
	public final void mT__82() throws RecognitionException {
		try {
			int _type = T__82;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:55:7: ( '@ENUM' )
			// JPA2.g:55:9: '@ENUM'
			{
			match("@ENUM"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__82"

	// $ANTLR start "T__83"
	public final void mT__83() throws RecognitionException {
		try {
			int _type = T__83;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:56:7: ( '@TODAY' )
			// JPA2.g:56:9: '@TODAY'
			{
			match("@TODAY"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__83"

	// $ANTLR start "T__84"
	public final void mT__84() throws RecognitionException {
		try {
			int _type = T__84;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:57:7: ( 'ABS(' )
			// JPA2.g:57:9: 'ABS('
			{
			match("ABS("); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__84"

	// $ANTLR start "T__85"
	public final void mT__85() throws RecognitionException {
		try {
			int _type = T__85;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:58:7: ( 'ALL' )
			// JPA2.g:58:9: 'ALL'
			{
			match("ALL"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__85"

	// $ANTLR start "T__86"
	public final void mT__86() throws RecognitionException {
		try {
			int _type = T__86;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:59:7: ( 'ANY' )
			// JPA2.g:59:9: 'ANY'
			{
			match("ANY"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__86"

	// $ANTLR start "T__87"
	public final void mT__87() throws RecognitionException {
		try {
			int _type = T__87;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:60:7: ( 'BETWEEN' )
			// JPA2.g:60:9: 'BETWEEN'
			{
			match("BETWEEN"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__87"

	// $ANTLR start "T__88"
	public final void mT__88() throws RecognitionException {
		try {
			int _type = T__88;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:61:7: ( 'BOTH' )
			// JPA2.g:61:9: 'BOTH'
			{
			match("BOTH"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__88"

	// $ANTLR start "T__89"
	public final void mT__89() throws RecognitionException {
		try {
			int _type = T__89;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:62:7: ( 'CAST(' )
			// JPA2.g:62:9: 'CAST('
			{
			match("CAST("); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__89"

	// $ANTLR start "T__90"
	public final void mT__90() throws RecognitionException {
		try {
			int _type = T__90;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:63:7: ( 'COALESCE(' )
			// JPA2.g:63:9: 'COALESCE('
			{
			match("COALESCE("); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__90"

	// $ANTLR start "T__91"
	public final void mT__91() throws RecognitionException {
		try {
			int _type = T__91;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:64:7: ( 'CONCAT(' )
			// JPA2.g:64:9: 'CONCAT('
			{
			match("CONCAT("); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__91"

	// $ANTLR start "T__92"
	public final void mT__92() throws RecognitionException {
		try {
			int _type = T__92;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:65:7: ( 'CURRENT_DATE' )
			// JPA2.g:65:9: 'CURRENT_DATE'
			{
			match("CURRENT_DATE"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__92"

	// $ANTLR start "T__93"
	public final void mT__93() throws RecognitionException {
		try {
			int _type = T__93;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:66:7: ( 'CURRENT_TIME' )
			// JPA2.g:66:9: 'CURRENT_TIME'
			{
			match("CURRENT_TIME"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__93"

	// $ANTLR start "T__94"
	public final void mT__94() throws RecognitionException {
		try {
			int _type = T__94;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:67:7: ( 'CURRENT_TIMESTAMP' )
			// JPA2.g:67:9: 'CURRENT_TIMESTAMP'
			{
			match("CURRENT_TIMESTAMP"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__94"

	// $ANTLR start "T__95"
	public final void mT__95() throws RecognitionException {
		try {
			int _type = T__95;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:68:7: ( 'DAY' )
			// JPA2.g:68:9: 'DAY'
			{
			match("DAY"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__95"

	// $ANTLR start "T__96"
	public final void mT__96() throws RecognitionException {
		try {
			int _type = T__96;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:69:7: ( 'DELETE' )
			// JPA2.g:69:9: 'DELETE'
			{
			match("DELETE"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__96"

	// $ANTLR start "T__97"
	public final void mT__97() throws RecognitionException {
		try {
			int _type = T__97;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:70:7: ( 'EMPTY' )
			// JPA2.g:70:9: 'EMPTY'
			{
			match("EMPTY"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__97"

	// $ANTLR start "T__98"
	public final void mT__98() throws RecognitionException {
		try {
			int _type = T__98;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:71:7: ( 'ENTRY(' )
			// JPA2.g:71:9: 'ENTRY('
			{
			match("ENTRY("); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__98"

	// $ANTLR start "T__99"
	public final void mT__99() throws RecognitionException {
		try {
			int _type = T__99;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:72:7: ( 'EPOCH' )
			// JPA2.g:72:9: 'EPOCH'
			{
			match("EPOCH"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__99"

	// $ANTLR start "T__100"
	public final void mT__100() throws RecognitionException {
		try {
			int _type = T__100;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:73:8: ( 'ESCAPE' )
			// JPA2.g:73:10: 'ESCAPE'
			{
			match("ESCAPE"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__100"

	// $ANTLR start "T__101"
	public final void mT__101() throws RecognitionException {
		try {
			int _type = T__101;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:74:8: ( 'EXISTS' )
			// JPA2.g:74:10: 'EXISTS'
			{
			match("EXISTS"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__101"

	// $ANTLR start "T__102"
	public final void mT__102() throws RecognitionException {
		try {
			int _type = T__102;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:75:8: ( 'EXTRACT(' )
			// JPA2.g:75:10: 'EXTRACT('
			{
			match("EXTRACT("); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__102"

	// $ANTLR start "T__103"
	public final void mT__103() throws RecognitionException {
		try {
			int _type = T__103;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:76:8: ( 'FROM' )
			// JPA2.g:76:10: 'FROM'
			{
			match("FROM"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__103"

	// $ANTLR start "T__104"
	public final void mT__104() throws RecognitionException {
		try {
			int _type = T__104;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:77:8: ( 'FUNCTION(' )
			// JPA2.g:77:10: 'FUNCTION('
			{
			match("FUNCTION("); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__104"

	// $ANTLR start "T__105"
	public final void mT__105() throws RecognitionException {
		try {
			int _type = T__105;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:78:8: ( 'HOUR' )
			// JPA2.g:78:10: 'HOUR'
			{
			match("HOUR"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__105"

	// $ANTLR start "T__106"
	public final void mT__106() throws RecognitionException {
		try {
			int _type = T__106;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:79:8: ( 'INDEX(' )
			// JPA2.g:79:10: 'INDEX('
			{
			match("INDEX("); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__106"

	// $ANTLR start "T__107"
	public final void mT__107() throws RecognitionException {
		try {
			int _type = T__107;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:80:8: ( 'IS' )
			// JPA2.g:80:10: 'IS'
			{
			match("IS"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__107"

	// $ANTLR start "T__108"
	public final void mT__108() throws RecognitionException {
		try {
			int _type = T__108;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:81:8: ( 'KEY(' )
			// JPA2.g:81:10: 'KEY('
			{
			match("KEY("); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__108"

	// $ANTLR start "T__109"
	public final void mT__109() throws RecognitionException {
		try {
			int _type = T__109;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:82:8: ( 'LEADING' )
			// JPA2.g:82:10: 'LEADING'
			{
			match("LEADING"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__109"

	// $ANTLR start "T__110"
	public final void mT__110() throws RecognitionException {
		try {
			int _type = T__110;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:83:8: ( 'LENGTH(' )
			// JPA2.g:83:10: 'LENGTH('
			{
			match("LENGTH("); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__110"

	// $ANTLR start "T__111"
	public final void mT__111() throws RecognitionException {
		try {
			int _type = T__111;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:84:8: ( 'LIKE' )
			// JPA2.g:84:10: 'LIKE'
			{
			match("LIKE"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__111"

	// $ANTLR start "T__112"
	public final void mT__112() throws RecognitionException {
		try {
			int _type = T__112;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:85:8: ( 'LOCATE(' )
			// JPA2.g:85:10: 'LOCATE('
			{
			match("LOCATE("); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__112"

	// $ANTLR start "T__113"
	public final void mT__113() throws RecognitionException {
		try {
			int _type = T__113;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:86:8: ( 'MEMBER' )
			// JPA2.g:86:10: 'MEMBER'
			{
			match("MEMBER"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__113"

	// $ANTLR start "T__114"
	public final void mT__114() throws RecognitionException {
		try {
			int _type = T__114;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:87:8: ( 'MINUTE' )
			// JPA2.g:87:10: 'MINUTE'
			{
			match("MINUTE"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__114"

	// $ANTLR start "T__115"
	public final void mT__115() throws RecognitionException {
		try {
			int _type = T__115;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:88:8: ( 'MOD(' )
			// JPA2.g:88:10: 'MOD('
			{
			match("MOD("); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__115"

	// $ANTLR start "T__116"
	public final void mT__116() throws RecognitionException {
		try {
			int _type = T__116;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:89:8: ( 'MONTH' )
			// JPA2.g:89:10: 'MONTH'
			{
			match("MONTH"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__116"

	// $ANTLR start "T__117"
	public final void mT__117() throws RecognitionException {
		try {
			int _type = T__117;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:90:8: ( 'NEW' )
			// JPA2.g:90:10: 'NEW'
			{
			match("NEW"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__117"

	// $ANTLR start "T__118"
	public final void mT__118() throws RecognitionException {
		try {
			int _type = T__118;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:91:8: ( 'NOW' )
			// JPA2.g:91:10: 'NOW'
			{
			match("NOW"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__118"

	// $ANTLR start "T__119"
	public final void mT__119() throws RecognitionException {
		try {
			int _type = T__119;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:92:8: ( 'NULL' )
			// JPA2.g:92:10: 'NULL'
			{
			match("NULL"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__119"

	// $ANTLR start "T__120"
	public final void mT__120() throws RecognitionException {
		try {
			int _type = T__120;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:93:8: ( 'NULLIF(' )
			// JPA2.g:93:10: 'NULLIF('
			{
			match("NULLIF("); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__120"

	// $ANTLR start "T__121"
	public final void mT__121() throws RecognitionException {
		try {
			int _type = T__121;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:94:8: ( 'NULLS FIRST' )
			// JPA2.g:94:10: 'NULLS FIRST'
			{
			match("NULLS FIRST"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__121"

	// $ANTLR start "T__122"
	public final void mT__122() throws RecognitionException {
		try {
			int _type = T__122;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:95:8: ( 'NULLS LAST' )
			// JPA2.g:95:10: 'NULLS LAST'
			{
			match("NULLS LAST"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__122"

	// $ANTLR start "T__123"
	public final void mT__123() throws RecognitionException {
		try {
			int _type = T__123;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:96:8: ( 'OBJECT' )
			// JPA2.g:96:10: 'OBJECT'
			{
			match("OBJECT"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__123"

	// $ANTLR start "T__124"
	public final void mT__124() throws RecognitionException {
		try {
			int _type = T__124;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:97:8: ( 'OF' )
			// JPA2.g:97:10: 'OF'
			{
			match("OF"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__124"

	// $ANTLR start "T__125"
	public final void mT__125() throws RecognitionException {
		try {
			int _type = T__125;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:98:8: ( 'ON' )
			// JPA2.g:98:10: 'ON'
			{
			match("ON"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__125"

	// $ANTLR start "T__126"
	public final void mT__126() throws RecognitionException {
		try {
			int _type = T__126;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:99:8: ( 'QUARTER' )
			// JPA2.g:99:10: 'QUARTER'
			{
			match("QUARTER"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__126"

	// $ANTLR start "T__127"
	public final void mT__127() throws RecognitionException {
		try {
			int _type = T__127;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:100:8: ( 'REGEXP' )
			// JPA2.g:100:10: 'REGEXP'
			{
			match("REGEXP"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__127"

	// $ANTLR start "T__128"
	public final void mT__128() throws RecognitionException {
		try {
			int _type = T__128;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:101:8: ( 'SECOND' )
			// JPA2.g:101:10: 'SECOND'
			{
			match("SECOND"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__128"

	// $ANTLR start "T__129"
	public final void mT__129() throws RecognitionException {
		try {
			int _type = T__129;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:102:8: ( 'SELECT' )
			// JPA2.g:102:10: 'SELECT'
			{
			match("SELECT"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__129"

	// $ANTLR start "T__130"
	public final void mT__130() throws RecognitionException {
		try {
			int _type = T__130;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:103:8: ( 'SIZE(' )
			// JPA2.g:103:10: 'SIZE('
			{
			match("SIZE("); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__130"

	// $ANTLR start "T__131"
	public final void mT__131() throws RecognitionException {
		try {
			int _type = T__131;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:104:8: ( 'SOME' )
			// JPA2.g:104:10: 'SOME'
			{
			match("SOME"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__131"

	// $ANTLR start "T__132"
	public final void mT__132() throws RecognitionException {
		try {
			int _type = T__132;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:105:8: ( 'SQRT(' )
			// JPA2.g:105:10: 'SQRT('
			{
			match("SQRT("); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__132"

	// $ANTLR start "T__133"
	public final void mT__133() throws RecognitionException {
		try {
			int _type = T__133;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:106:8: ( 'SUBSTRING(' )
			// JPA2.g:106:10: 'SUBSTRING('
			{
			match("SUBSTRING("); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__133"

	// $ANTLR start "T__134"
	public final void mT__134() throws RecognitionException {
		try {
			int _type = T__134;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:107:8: ( 'TRAILING' )
			// JPA2.g:107:10: 'TRAILING'
			{
			match("TRAILING"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__134"

	// $ANTLR start "T__135"
	public final void mT__135() throws RecognitionException {
		try {
			int _type = T__135;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:108:8: ( 'TREAT(' )
			// JPA2.g:108:10: 'TREAT('
			{
			match("TREAT("); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__135"

	// $ANTLR start "T__136"
	public final void mT__136() throws RecognitionException {
		try {
			int _type = T__136;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:109:8: ( 'TRIM(' )
			// JPA2.g:109:10: 'TRIM('
			{
			match("TRIM("); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__136"

	// $ANTLR start "T__137"
	public final void mT__137() throws RecognitionException {
		try {
			int _type = T__137;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:110:8: ( 'TYPE(' )
			// JPA2.g:110:10: 'TYPE('
			{
			match("TYPE("); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__137"

	// $ANTLR start "T__138"
	public final void mT__138() throws RecognitionException {
		try {
			int _type = T__138;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:111:8: ( 'UPDATE' )
			// JPA2.g:111:10: 'UPDATE'
			{
			match("UPDATE"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__138"

	// $ANTLR start "T__139"
	public final void mT__139() throws RecognitionException {
		try {
			int _type = T__139;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:112:8: ( 'UPPER(' )
			// JPA2.g:112:10: 'UPPER('
			{
			match("UPPER("); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__139"

	// $ANTLR start "T__140"
	public final void mT__140() throws RecognitionException {
		try {
			int _type = T__140;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:113:8: ( 'USER_TIMEZONE' )
			// JPA2.g:113:10: 'USER_TIMEZONE'
			{
			match("USER_TIMEZONE"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__140"

	// $ANTLR start "T__141"
	public final void mT__141() throws RecognitionException {
		try {
			int _type = T__141;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:114:8: ( 'VALUE(' )
			// JPA2.g:114:10: 'VALUE('
			{
			match("VALUE("); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__141"

	// $ANTLR start "T__142"
	public final void mT__142() throws RecognitionException {
		try {
			int _type = T__142;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:115:8: ( 'WEEK' )
			// JPA2.g:115:10: 'WEEK'
			{
			match("WEEK"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__142"

	// $ANTLR start "T__143"
	public final void mT__143() throws RecognitionException {
		try {
			int _type = T__143;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:116:8: ( 'WHERE' )
			// JPA2.g:116:10: 'WHERE'
			{
			match("WHERE"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__143"

	// $ANTLR start "T__144"
	public final void mT__144() throws RecognitionException {
		try {
			int _type = T__144;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:117:8: ( 'YEAR' )
			// JPA2.g:117:10: 'YEAR'
			{
			match("YEAR"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__144"

	// $ANTLR start "T__145"
	public final void mT__145() throws RecognitionException {
		try {
			int _type = T__145;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:118:8: ( 'false' )
			// JPA2.g:118:10: 'false'
			{
			match("false"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__145"

	// $ANTLR start "T__146"
	public final void mT__146() throws RecognitionException {
		try {
			int _type = T__146;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:119:8: ( 'true' )
			// JPA2.g:119:10: 'true'
			{
			match("true"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__146"

	// $ANTLR start "T__147"
	public final void mT__147() throws RecognitionException {
		try {
			int _type = T__147;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:120:8: ( '}' )
			// JPA2.g:120:10: '}'
			{
			match('}'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__147"

	// $ANTLR start "NOT"
	public final void mNOT() throws RecognitionException {
		try {
			int _type = NOT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:318:4: ( 'NOT' )
			// JPA2.g:318:6: 'NOT'
			{
				match("NOT");

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "NOT"

	// $ANTLR start "IN"
	public final void mIN() throws RecognitionException {
		try {
			int _type = IN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:320:3: ( 'IN' )
			// JPA2.g:320:5: 'IN'
			{
				match("IN");

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "IN"

	// $ANTLR start "TRIM_CHARACTER"
	public final void mTRIM_CHARACTER() throws RecognitionException {
		try {
			int _type = TRIM_CHARACTER;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:569:5: ( '\\'.\\'' )
			// JPA2.g:569:7: '\\'.\\''
			{
				match("'.'");

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "TRIM_CHARACTER"

	// $ANTLR start "STRING_LITERAL"
	public final void mSTRING_LITERAL() throws RecognitionException {
		try {
			int _type = STRING_LITERAL;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:572:5: ( '\\'' (~ ( '\\'' | '\"' ) )* '\\'' )
			// JPA2.g:572:7: '\\'' (~ ( '\\'' | '\"' ) )* '\\''
			{
				match('\'');
				// JPA2.g:572:12: (~ ( '\\'' | '\"' ) )*
				loop1:
				while (true) {
					int alt1 = 2;
					int LA1_0 = input.LA(1);
					if (((LA1_0 >= '\u0000' && LA1_0 <= '!') || (LA1_0 >= '#' && LA1_0 <= '&') || (LA1_0 >= '(' && LA1_0 <= '\uFFFF'))) {
						alt1 = 1;
					}

					switch (alt1) {
						case 1:
							// JPA2.g:
						{
							if ((input.LA(1) >= '\u0000' && input.LA(1) <= '!') || (input.LA(1) >= '#' && input.LA(1) <= '&') || (input.LA(1) >= '(' && input.LA(1) <= '\uFFFF')) {
								input.consume();
							} else {
								MismatchedSetException mse = new MismatchedSetException(null, input);
								recover(mse);
								throw mse;
							}
						}
						break;

						default:
							break loop1;
					}
				}

			match('\''); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "STRING_LITERAL"

	// $ANTLR start "WORD"
	public final void mWORD() throws RecognitionException {
		try {
			int _type = WORD;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:575:5: ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '$' )* )
			// JPA2.g:575:7: ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '$' )*
			{
				if ((input.LA(1) >= 'A' && input.LA(1) <= 'Z') || input.LA(1) == '_' || (input.LA(1) >= 'a' && input.LA(1) <= 'z')) {
					input.consume();
				} else {
					MismatchedSetException mse = new MismatchedSetException(null, input);
					recover(mse);
					throw mse;
				}
				// JPA2.g:575:31: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '$' )*
				loop2:
				while (true) {
					int alt2 = 2;
					int LA2_0 = input.LA(1);
					if ((LA2_0 == '$' || (LA2_0 >= '0' && LA2_0 <= '9') || (LA2_0 >= 'A' && LA2_0 <= 'Z') || LA2_0 == '_' || (LA2_0 >= 'a' && LA2_0 <= 'z'))) {
						alt2 = 1;
					}

					switch (alt2) {
						case 1:
							// JPA2.g:
						{
							if (input.LA(1) == '$' || (input.LA(1) >= '0' && input.LA(1) <= '9') || (input.LA(1) >= 'A' && input.LA(1) <= 'Z') || input.LA(1) == '_' || (input.LA(1) >= 'a' && input.LA(1) <= 'z')) {
								input.consume();
							} else {
								MismatchedSetException mse = new MismatchedSetException(null, input);
								recover(mse);
								throw mse;
							}
						}
						break;

						default:
							break loop2;
					}
				}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "WORD"

	// $ANTLR start "RUSSIAN_SYMBOLS"
	public final void mRUSSIAN_SYMBOLS() throws RecognitionException {
		try {
			int _type = RUSSIAN_SYMBOLS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:578:5: ( ( '\\u0400' .. '\\u04FF' | '\\u0500' .. '\\u052F' ) )
			// JPA2.g:578:7: ( '\\u0400' .. '\\u04FF' | '\\u0500' .. '\\u052F' )
			{
				if ((input.LA(1) >= '\u0400' && input.LA(1) <= '\u052F')) {
					input.consume();
				} else {
					MismatchedSetException mse = new MismatchedSetException(null, input);
					recover(mse);
					throw mse;
				}
				if (1 == 1) throw new IllegalArgumentException("Incorrect symbol");
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "RUSSIAN_SYMBOLS"

	// $ANTLR start "NAMED_PARAMETER"
	public final void mNAMED_PARAMETER() throws RecognitionException {
		try {
			int _type = NAMED_PARAMETER;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:581:5: ( ':' ( '(?i)' | '(?I)' )? ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '$' )* ( ( '.' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '$' )+ )* )
			// JPA2.g:581:7: ':' ( '(?i)' | '(?I)' )? ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '$' )* ( ( '.' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '$' )+ )*
			{
				match(':');
				// JPA2.g:581:11: ( '(?i)' | '(?I)' )?
				int alt3 = 3;
				int LA3_0 = input.LA(1);
				if ((LA3_0 == '(')) {
					int LA3_1 = input.LA(2);
					if ((LA3_1 == '?')) {
						int LA3_3 = input.LA(3);
						if ((LA3_3 == 'i')) {
							alt3 = 1;
						} else if ((LA3_3 == 'I')) {
							alt3 = 2;
						}
					}
				}
				switch (alt3) {
					case 1:
						// JPA2.g:581:12: '(?i)'
					{
						match("(?i)");

					}
					break;
					case 2:
						// JPA2.g:581:19: '(?I)'
					{
						match("(?I)");

					}
					break;

				}

				if ((input.LA(1) >= 'A' && input.LA(1) <= 'Z') || input.LA(1) == '_' || (input.LA(1) >= 'a' && input.LA(1) <= 'z')) {
					input.consume();
				} else {
					MismatchedSetException mse = new MismatchedSetException(null, input);
					recover(mse);
					throw mse;
				}
				// JPA2.g:581:52: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '$' )*
				loop4:
				while (true) {
					int alt4 = 2;
					int LA4_0 = input.LA(1);
					if ((LA4_0 == '$' || (LA4_0 >= '0' && LA4_0 <= '9') || (LA4_0 >= 'A' && LA4_0 <= 'Z') || LA4_0 == '_' || (LA4_0 >= 'a' && LA4_0 <= 'z'))) {
						alt4 = 1;
					}

					switch (alt4) {
						case 1:
							// JPA2.g:
						{
							if (input.LA(1) == '$' || (input.LA(1) >= '0' && input.LA(1) <= '9') || (input.LA(1) >= 'A' && input.LA(1) <= 'Z') || input.LA(1) == '_' || (input.LA(1) >= 'a' && input.LA(1) <= 'z')) {
								input.consume();
							} else {
								MismatchedSetException mse = new MismatchedSetException(null, input);
								recover(mse);
								throw mse;
							}
						}
						break;

						default:
							break loop4;
					}
				}

				// JPA2.g:581:90: ( ( '.' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '$' )+ )*
				loop6:
				while (true) {
					int alt6 = 2;
					int LA6_0 = input.LA(1);
					if ((LA6_0 == '.')) {
						alt6 = 1;
					}

					switch (alt6) {
						case 1:
							// JPA2.g:581:91: ( '.' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '$' )+
						{
							// JPA2.g:581:91: ( '.' )
							// JPA2.g:581:92: '.'
							{
								match('.');
							}

							// JPA2.g:581:97: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '$' )+
							int cnt5 = 0;
							loop5:
							while (true) {
								int alt5 = 2;
								int LA5_0 = input.LA(1);
								if ((LA5_0 == '$' || (LA5_0 >= '0' && LA5_0 <= '9') || (LA5_0 >= 'A' && LA5_0 <= 'Z') || LA5_0 == '_' || (LA5_0 >= 'a' && LA5_0 <= 'z'))) {
									alt5 = 1;
								}

								switch (alt5) {
									case 1:
										// JPA2.g:
									{
										if (input.LA(1) == '$' || (input.LA(1) >= '0' && input.LA(1) <= '9') || (input.LA(1) >= 'A' && input.LA(1) <= 'Z') || input.LA(1) == '_' || (input.LA(1) >= 'a' && input.LA(1) <= 'z')) {
											input.consume();
										} else {
											MismatchedSetException mse = new MismatchedSetException(null, input);
											recover(mse);
											throw mse;
										}
									}
									break;

									default:
										if (cnt5 >= 1) break loop5;
										EarlyExitException eee = new EarlyExitException(5, input);
										throw eee;
								}
								cnt5++;
							}

					}
					break;

				default :
					break loop6;
				}
			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "NAMED_PARAMETER"

	// $ANTLR start "WS"
	public final void mWS() throws RecognitionException {
		try {
			int _type = WS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:583:5: ( ( ' ' | '\\r' | '\\t' | '\\u000C' | '\\n' ) )
			// JPA2.g:583:7: ( ' ' | '\\r' | '\\t' | '\\u000C' | '\\n' )
			{
				if ((input.LA(1) >= '\t' && input.LA(1) <= '\n') || (input.LA(1) >= '\f' && input.LA(1) <= '\r') || input.LA(1) == ' ') {
					input.consume();
				} else {
					MismatchedSetException mse = new MismatchedSetException(null, input);
					recover(mse);
					throw mse;
				}
				_channel = HIDDEN;
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "WS"

	// $ANTLR start "COMMENT"
	public final void mCOMMENT() throws RecognitionException {
		try {
			int _type = COMMENT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:587:5: ( '/*' ( . )* '*/' )
			// JPA2.g:587:7: '/*' ( . )* '*/'
			{
				match("/*");

				// JPA2.g:587:12: ( . )*
				loop7:
				while (true) {
					int alt7 = 2;
					int LA7_0 = input.LA(1);
					if ((LA7_0 == '*')) {
						int LA7_1 = input.LA(2);
						if ((LA7_1 == '/')) {
							alt7 = 2;
						} else if (((LA7_1 >= '\u0000' && LA7_1 <= '.') || (LA7_1 >= '0' && LA7_1 <= '\uFFFF'))) {
							alt7 = 1;
						}

					} else if (((LA7_0 >= '\u0000' && LA7_0 <= ')') || (LA7_0 >= '+' && LA7_0 <= '\uFFFF'))) {
						alt7 = 1;
					}

					switch (alt7) {
						case 1:
							// JPA2.g:587:12: .
						{
							matchAny();
						}
						break;

						default:
							break loop7;
					}
				}

			match("*/"); 

			_channel=HIDDEN;
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "COMMENT"

	// $ANTLR start "LINE_COMMENT"
	public final void mLINE_COMMENT() throws RecognitionException {
		try {
			int _type = LINE_COMMENT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:590:5: ( '//' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n' )
			// JPA2.g:590:7: '//' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n'
			{
				match("//");

				// JPA2.g:590:12: (~ ( '\\n' | '\\r' ) )*
				loop8:
				while (true) {
					int alt8 = 2;
					int LA8_0 = input.LA(1);
					if (((LA8_0 >= '\u0000' && LA8_0 <= '\t') || (LA8_0 >= '\u000B' && LA8_0 <= '\f') || (LA8_0 >= '\u000E' && LA8_0 <= '\uFFFF'))) {
						alt8 = 1;
					}

					switch (alt8) {
						case 1:
							// JPA2.g:
						{
							if ((input.LA(1) >= '\u0000' && input.LA(1) <= '\t') || (input.LA(1) >= '\u000B' && input.LA(1) <= '\f') || (input.LA(1) >= '\u000E' && input.LA(1) <= '\uFFFF')) {
								input.consume();
							} else {
								MismatchedSetException mse = new MismatchedSetException(null, input);
								recover(mse);
								throw mse;
							}
						}
						break;

						default:
							break loop8;
					}
				}

				// JPA2.g:590:26: ( '\\r' )?
				int alt9 = 2;
				int LA9_0 = input.LA(1);
				if ((LA9_0 == '\r')) {
					alt9 = 1;
				}
				switch (alt9) {
					case 1:
						// JPA2.g:590:26: '\\r'
					{
						match('\r');
					}
					break;

				}

				match('\n');
				_channel = HIDDEN;
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "LINE_COMMENT"

	// $ANTLR start "ESCAPE_CHARACTER"
	public final void mESCAPE_CHARACTER() throws RecognitionException {
		try {
			int _type = ESCAPE_CHARACTER;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:593:5: ( '\\'' (~ ( '\\'' | '\\\\' ) ) '\\'' )
			// JPA2.g:593:7: '\\'' (~ ( '\\'' | '\\\\' ) ) '\\''
			{
				match('\'');
				if ((input.LA(1) >= '\u0000' && input.LA(1) <= '&') || (input.LA(1) >= '(' && input.LA(1) <= '[') || (input.LA(1) >= ']' && input.LA(1) <= '\uFFFF')) {
					input.consume();
				} else {
					MismatchedSetException mse = new MismatchedSetException(null, input);
					recover(mse);
					throw mse;
				}
				match('\'');
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "ESCAPE_CHARACTER"

	// $ANTLR start "INT_NUMERAL"
	public final void mINT_NUMERAL() throws RecognitionException {
		try {
			int _type = INT_NUMERAL;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// JPA2.g:596:5: ( ( '0' .. '9' )+ )
			// JPA2.g:596:7: ( '0' .. '9' )+
			{
				// JPA2.g:596:7: ( '0' .. '9' )+
				int cnt10 = 0;
				loop10:
				while (true) {
					int alt10 = 2;
					int LA10_0 = input.LA(1);
					if (((LA10_0 >= '0' && LA10_0 <= '9'))) {
						alt10 = 1;
					}

					switch (alt10) {
						case 1:
							// JPA2.g:
						{
							if ((input.LA(1) >= '0' && input.LA(1) <= '9')) {
								input.consume();
							} else {
								MismatchedSetException mse = new MismatchedSetException(null, input);
								recover(mse);
								throw mse;
							}
						}
						break;

						default:
							if (cnt10 >= 1) break loop10;
							EarlyExitException eee = new EarlyExitException(10, input);
							throw eee;
					}
					cnt10++;
				}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "INT_NUMERAL"

	@Override
	public void mTokens() throws RecognitionException {
		// JPA2.g:1:8: ( AND | AS | ASC | AVG | BY | CASE | COUNT | DESC | DISTINCT | ELSE | END | FETCH | GROUP | HAVING | INNER | JOIN | LEFT | LOWER | LPAREN | MAX | MIN | OR | ORDER | OUTER | RPAREN | SET | SUM | THEN | WHEN | T__63 | T__64 | T__65 | T__66 | T__67 | T__68 | T__69 | T__70 | T__71 | T__72 | T__73 | T__74 | T__75 | T__76 | T__77 | T__78 | T__79 | T__80 | T__81 | T__82 | T__83 | T__84 | T__85 | T__86 | T__87 | T__88 | T__89 | T__90 | T__91 | T__92 | T__93 | T__94 | T__95 | T__96 | T__97 | T__98 | T__99 | T__100 | T__101 | T__102 | T__103 | T__104 | T__105 | T__106 | T__107 | T__108 | T__109 | T__110 | T__111 | T__112 | T__113 | T__114 | T__115 | T__116 | T__117 | T__118 | T__119 | T__120 | T__121 | T__122 | T__123 | T__124 | T__125 | T__126 | T__127 | T__128 | T__129 | T__130 | T__131 | T__132 | T__133 | T__134 | T__135 | T__136 | T__137 | T__138 | T__139 | T__140 | T__141 | T__142 | T__143 | T__144 | T__145 | T__146 | T__147 | NOT | IN | TRIM_CHARACTER | STRING_LITERAL | WORD | RUSSIAN_SYMBOLS | NAMED_PARAMETER | WS | COMMENT | LINE_COMMENT | ESCAPE_CHARACTER | INT_NUMERAL )
		int alt11=126;
		alt11 = dfa11.predict(input);
		switch (alt11) {
			case 1 :
				// JPA2.g:1:10: AND
				{
				mAND(); 

				}
				break;
			case 2 :
				// JPA2.g:1:14: AS
				{
				mAS(); 

				}
				break;
			case 3 :
				// JPA2.g:1:17: ASC
				{
				mASC(); 

				}
				break;
			case 4 :
				// JPA2.g:1:21: AVG
				{
				mAVG(); 

				}
				break;
			case 5 :
				// JPA2.g:1:25: BY
				{
				mBY(); 

				}
				break;
			case 6 :
				// JPA2.g:1:28: CASE
				{
				mCASE(); 

				}
				break;
			case 7 :
				// JPA2.g:1:33: COUNT
				{
				mCOUNT(); 

				}
				break;
			case 8 :
				// JPA2.g:1:39: DESC
				{
				mDESC(); 

				}
				break;
			case 9 :
				// JPA2.g:1:44: DISTINCT
				{
				mDISTINCT(); 

				}
				break;
			case 10 :
				// JPA2.g:1:53: ELSE
				{
				mELSE(); 

				}
				break;
			case 11 :
				// JPA2.g:1:58: END
				{
				mEND(); 

				}
				break;
			case 12 :
				// JPA2.g:1:62: FETCH
				{
				mFETCH(); 

				}
				break;
			case 13 :
				// JPA2.g:1:68: GROUP
				{
				mGROUP(); 

				}
				break;
			case 14 :
				// JPA2.g:1:74: HAVING
				{
				mHAVING(); 

				}
				break;
			case 15 :
				// JPA2.g:1:81: INNER
				{
				mINNER(); 

				}
				break;
			case 16 :
				// JPA2.g:1:87: JOIN
				{
				mJOIN(); 

				}
				break;
			case 17 :
				// JPA2.g:1:92: LEFT
				{
				mLEFT(); 

				}
				break;
			case 18 :
				// JPA2.g:1:97: LOWER
				{
				mLOWER(); 

				}
				break;
			case 19 :
				// JPA2.g:1:103: LPAREN
				{
				mLPAREN(); 

				}
				break;
			case 20 :
				// JPA2.g:1:110: MAX
				{
				mMAX(); 

				}
				break;
			case 21 :
				// JPA2.g:1:114: MIN
				{
				mMIN(); 

				}
				break;
			case 22 :
				// JPA2.g:1:118: OR
				{
				mOR(); 

				}
				break;
			case 23 :
				// JPA2.g:1:121: ORDER
				{
				mORDER(); 

				}
				break;
			case 24 :
				// JPA2.g:1:127: OUTER
				{
				mOUTER(); 

				}
				break;
			case 25 :
				// JPA2.g:1:133: RPAREN
				{
				mRPAREN(); 

				}
				break;
			case 26 :
				// JPA2.g:1:140: SET
				{
				mSET(); 

				}
				break;
			case 27 :
				// JPA2.g:1:144: SUM
				{
				mSUM(); 

				}
				break;
			case 28 :
				// JPA2.g:1:148: THEN
				{
				mTHEN(); 

				}
				break;
			case 29 :
				// JPA2.g:1:153: WHEN
				{
				mWHEN(); 

				}
				break;
			case 30 :
				// JPA2.g:1:158: T__63
				{
				mT__63(); 

				}
				break;
			case 31 :
				// JPA2.g:1:164: T__64
				{
				mT__64(); 

				}
				break;
			case 32 :
				// JPA2.g:1:170: T__65
				{
				mT__65(); 

				}
				break;
			case 33 :
				// JPA2.g:1:176: T__66
				{
				mT__66(); 

				}
				break;
			case 34 :
				// JPA2.g:1:182: T__67
				{
				mT__67(); 

				}
				break;
			case 35 :
				// JPA2.g:1:188: T__68
				{
				mT__68(); 

				}
				break;
			case 36 :
				// JPA2.g:1:194: T__69
				{
				mT__69(); 

				}
				break;
			case 37 :
				// JPA2.g:1:200: T__70
				{
				mT__70(); 

				}
				break;
			case 38 :
				// JPA2.g:1:206: T__71
				{
				mT__71(); 

				}
				break;
			case 39 :
				// JPA2.g:1:212: T__72
				{
				mT__72(); 

				}
				break;
			case 40 :
				// JPA2.g:1:218: T__73
				{
				mT__73(); 

				}
				break;
			case 41 :
				// JPA2.g:1:224: T__74
				{
				mT__74(); 

				}
				break;
			case 42 :
				// JPA2.g:1:230: T__75
				{
				mT__75(); 

				}
				break;
			case 43 :
				// JPA2.g:1:236: T__76
				{
				mT__76(); 

				}
				break;
			case 44 :
				// JPA2.g:1:242: T__77
				{
				mT__77(); 

				}
				break;
			case 45 :
				// JPA2.g:1:248: T__78
				{
				mT__78(); 

				}
				break;
			case 46 :
				// JPA2.g:1:254: T__79
				{
				mT__79(); 

				}
				break;
			case 47 :
				// JPA2.g:1:260: T__80
				{
				mT__80(); 

				}
				break;
			case 48 :
				// JPA2.g:1:266: T__81
				{
				mT__81(); 

				}
				break;
			case 49 :
				// JPA2.g:1:272: T__82
				{
				mT__82(); 

				}
				break;
			case 50 :
				// JPA2.g:1:278: T__83
				{
				mT__83(); 

				}
				break;
			case 51 :
				// JPA2.g:1:284: T__84
				{
				mT__84(); 

				}
				break;
			case 52 :
				// JPA2.g:1:290: T__85
				{
				mT__85(); 

				}
				break;
			case 53 :
				// JPA2.g:1:296: T__86
				{
				mT__86(); 

				}
				break;
			case 54 :
				// JPA2.g:1:302: T__87
				{
				mT__87(); 

				}
				break;
			case 55 :
				// JPA2.g:1:308: T__88
				{
				mT__88(); 

				}
				break;
			case 56 :
				// JPA2.g:1:314: T__89
				{
				mT__89(); 

				}
				break;
			case 57 :
				// JPA2.g:1:320: T__90
				{
				mT__90(); 

				}
				break;
			case 58 :
				// JPA2.g:1:326: T__91
				{
				mT__91(); 

				}
				break;
			case 59 :
				// JPA2.g:1:332: T__92
				{
				mT__92(); 

				}
				break;
			case 60 :
				// JPA2.g:1:338: T__93
				{
				mT__93(); 

				}
				break;
			case 61 :
				// JPA2.g:1:344: T__94
				{
				mT__94(); 

				}
				break;
			case 62 :
				// JPA2.g:1:350: T__95
				{
				mT__95(); 

				}
				break;
			case 63 :
				// JPA2.g:1:356: T__96
				{
				mT__96(); 

				}
				break;
			case 64 :
				// JPA2.g:1:362: T__97
				{
				mT__97(); 

				}
				break;
			case 65 :
				// JPA2.g:1:368: T__98
				{
				mT__98(); 

				}
				break;
			case 66 :
				// JPA2.g:1:374: T__99
				{
				mT__99(); 

				}
				break;
			case 67 :
				// JPA2.g:1:380: T__100
				{
				mT__100(); 

				}
				break;
			case 68 :
				// JPA2.g:1:387: T__101
				{
				mT__101(); 

				}
				break;
			case 69 :
				// JPA2.g:1:394: T__102
				{
				mT__102(); 

				}
				break;
			case 70 :
				// JPA2.g:1:401: T__103
				{
				mT__103(); 

				}
				break;
			case 71 :
				// JPA2.g:1:408: T__104
				{
				mT__104(); 

				}
				break;
			case 72 :
				// JPA2.g:1:415: T__105
				{
				mT__105(); 

				}
				break;
			case 73 :
				// JPA2.g:1:422: T__106
				{
				mT__106(); 

				}
				break;
			case 74 :
				// JPA2.g:1:429: T__107
				{
				mT__107(); 

				}
				break;
			case 75 :
				// JPA2.g:1:436: T__108
				{
				mT__108(); 

				}
				break;
			case 76 :
				// JPA2.g:1:443: T__109
				{
				mT__109(); 

				}
				break;
			case 77 :
				// JPA2.g:1:450: T__110
				{
				mT__110(); 

				}
				break;
			case 78 :
				// JPA2.g:1:457: T__111
				{
				mT__111(); 

				}
				break;
			case 79 :
				// JPA2.g:1:464: T__112
				{
				mT__112(); 

				}
				break;
			case 80 :
				// JPA2.g:1:471: T__113
				{
				mT__113(); 

				}
				break;
			case 81 :
				// JPA2.g:1:478: T__114
				{
				mT__114(); 

				}
				break;
			case 82 :
				// JPA2.g:1:485: T__115
				{
				mT__115(); 

				}
				break;
			case 83 :
				// JPA2.g:1:492: T__116
				{
				mT__116(); 

				}
				break;
			case 84 :
				// JPA2.g:1:499: T__117
				{
				mT__117(); 

				}
				break;
			case 85 :
				// JPA2.g:1:506: T__118
				{
				mT__118(); 

				}
				break;
			case 86 :
				// JPA2.g:1:513: T__119
				{
				mT__119(); 

				}
				break;
			case 87 :
				// JPA2.g:1:520: T__120
				{
				mT__120(); 

				}
				break;
			case 88 :
				// JPA2.g:1:527: T__121
				{
				mT__121(); 

				}
				break;
			case 89 :
				// JPA2.g:1:534: T__122
				{
				mT__122(); 

				}
				break;
			case 90 :
				// JPA2.g:1:541: T__123
				{
				mT__123(); 

				}
				break;
			case 91 :
				// JPA2.g:1:548: T__124
				{
				mT__124(); 

				}
				break;
			case 92 :
				// JPA2.g:1:555: T__125
				{
				mT__125(); 

				}
				break;
			case 93 :
				// JPA2.g:1:562: T__126
				{
				mT__126(); 

				}
				break;
			case 94 :
				// JPA2.g:1:569: T__127
				{
				mT__127(); 

				}
				break;
			case 95 :
				// JPA2.g:1:576: T__128
				{
				mT__128(); 

				}
				break;
			case 96 :
				// JPA2.g:1:583: T__129
				{
				mT__129(); 

				}
				break;
			case 97 :
				// JPA2.g:1:590: T__130
				{
				mT__130(); 

				}
				break;
			case 98 :
				// JPA2.g:1:597: T__131
				{
				mT__131(); 

				}
				break;
			case 99 :
				// JPA2.g:1:604: T__132
				{
				mT__132(); 

				}
				break;
			case 100 :
				// JPA2.g:1:611: T__133
				{
				mT__133(); 

				}
				break;
			case 101 :
				// JPA2.g:1:618: T__134
				{
				mT__134(); 

				}
				break;
			case 102 :
				// JPA2.g:1:625: T__135
				{
				mT__135(); 

				}
				break;
			case 103 :
				// JPA2.g:1:632: T__136
				{
				mT__136(); 

				}
				break;
			case 104 :
				// JPA2.g:1:639: T__137
				{
				mT__137(); 

				}
				break;
			case 105 :
				// JPA2.g:1:646: T__138
				{
				mT__138(); 

				}
				break;
			case 106 :
				// JPA2.g:1:653: T__139
				{
				mT__139(); 

				}
				break;
			case 107 :
				// JPA2.g:1:660: T__140
				{
				mT__140(); 

				}
				break;
			case 108 :
				// JPA2.g:1:667: T__141
				{
				mT__141(); 

				}
				break;
			case 109 :
				// JPA2.g:1:674: T__142
				{
				mT__142(); 

				}
				break;
			case 110 :
				// JPA2.g:1:681: T__143
				{
				mT__143(); 

				}
				break;
			case 111 :
				// JPA2.g:1:688: T__144
				{
				mT__144(); 

				}
				break;
			case 112 :
				// JPA2.g:1:695: T__145
				{
				mT__145(); 

				}
				break;
			case 113 :
				// JPA2.g:1:702: T__146
				{
				mT__146(); 

				}
				break;
			case 114 :
				// JPA2.g:1:709: T__147
				{
				mT__147(); 

				}
				break;
			case 115 :
				// JPA2.g:1:716: NOT
				{
				mNOT(); 

				}
				break;
			case 116 :
				// JPA2.g:1:720: IN
				{
				mIN(); 

				}
				break;
			case 117 :
				// JPA2.g:1:723: TRIM_CHARACTER
				{
				mTRIM_CHARACTER(); 

				}
				break;
			case 118 :
				// JPA2.g:1:738: STRING_LITERAL
				{
				mSTRING_LITERAL(); 

				}
				break;
			case 119 :
				// JPA2.g:1:753: WORD
				{
				mWORD(); 

				}
				break;
			case 120 :
				// JPA2.g:1:758: RUSSIAN_SYMBOLS
				{
				mRUSSIAN_SYMBOLS(); 

				}
				break;
			case 121 :
				// JPA2.g:1:774: NAMED_PARAMETER
				{
				mNAMED_PARAMETER(); 

				}
				break;
			case 122 :
				// JPA2.g:1:790: WS
				{
				mWS(); 

				}
				break;
			case 123 :
				// JPA2.g:1:793: COMMENT
				{
				mCOMMENT(); 

				}
				break;
			case 124 :
				// JPA2.g:1:801: LINE_COMMENT
				{
				mLINE_COMMENT(); 

				}
				break;
			case 125 :
				// JPA2.g:1:814: ESCAPE_CHARACTER
				{
				mESCAPE_CHARACTER(); 

				}
				break;
			case 126 :
				// JPA2.g:1:831: INT_NUMERAL
				{
				mINT_NUMERAL(); 

				}
				break;

		}
	}


	protected DFA11 dfa11 = new DFA11(this);
	static final String DFA11_eotS =
		"\1\uffff\13\53\1\uffff\2\53\1\uffff\3\53\6\uffff\1\145\1\57\1\151\1\uffff"+
		"\1\153\2\uffff\11\53\7\uffff\1\53\1\u0083\3\53\1\u0087\24\53\1\u00a3\1"+
		"\u00a4\10\53\1\u00b2\2\53\1\u00b5\1\u00b6\12\53\15\uffff\14\53\4\uffff"+
		"\1\u00d7\1\u00d8\1\u00d9\1\uffff\1\u00da\1\53\1\u00dc\1\uffff\12\53\1"+
		"\u00e8\1\53\1\u00ea\16\53\2\uffff\7\53\1\u0100\1\u0102\4\53\1\uffff\2"+
		"\53\2\uffff\1\u0109\2\53\1\u010c\13\53\1\uffff\1\53\1\u011b\1\u011c\1"+
		"\u011d\12\53\10\uffff\1\53\1\u012a\1\u012b\5\53\1\u0131\2\53\1\uffff\1"+
		"\u0134\1\uffff\7\53\1\u013c\3\53\1\u0140\2\53\1\u0143\1\u0144\4\53\1\u0149"+
		"\1\uffff\1\53\1\uffff\1\53\1\uffff\4\53\1\uffff\2\53\1\uffff\2\53\1\u0154"+
		"\1\53\1\u0156\4\53\1\u015b\1\53\1\u015d\5\uffff\1\u0161\6\53\1\u0168\1"+
		"\53\1\u016a\1\uffff\1\53\3\uffff\1\u016c\3\53\1\uffff\2\53\1\uffff\1\53"+
		"\1\u0173\1\u0174\3\53\1\u0178\1\uffff\1\53\1\u017a\1\53\1\uffff\1\u017c"+
		"\1\53\2\uffff\2\53\1\u0180\1\53\1\uffff\2\53\1\u0184\1\u0185\1\u0186\4"+
		"\53\4\uffff\2\53\3\uffff\1\u018d\2\uffff\2\53\1\uffff\6\53\1\uffff\1\u0199"+
		"\1\uffff\1\53\1\uffff\3\53\1\u019e\1\53\3\uffff\1\u01a0\1\u01a1\1\53\1"+
		"\uffff\1\53\1\uffff\1\u01a4\2\uffff\2\53\1\uffff\1\53\1\u01a8\1\u01a9"+
		"\3\uffff\1\u01aa\1\u01ab\1\u01ac\2\53\5\uffff\1\53\1\uffff\1\53\1\u01b3"+
		"\1\u01b4\1\uffff\1\53\2\uffff\1\u01b6\1\53\1\uffff\1\53\1\uffff\1\53\2"+
		"\uffff\2\53\1\uffff\1\u01bc\7\uffff\2\53\3\uffff\1\u01bf\2\uffff\1\53"+
		"\1\uffff\2\53\1\u01c4\1\uffff\1\53\1\uffff\1\53\1\u01c7\1\uffff\1\53\1"+
		"\uffff\2\53\2\uffff\1\53\1\uffff\3\53\1\uffff\4\53\1\u01d3\1\u01d5\1\53"+
		"\1\uffff\1\53\1\uffff\1\u01d8\1\53\1\uffff\2\53\1\u01dc\1\uffff";
	static final String DFA11_eofS =
		"\u01dd\uffff";
	static final String DFA11_minS =
		"\1\11\1\102\1\105\2\101\1\114\1\105\1\122\1\101\1\116\1\117\1\105\1\uffff"+
		"\1\101\1\102\1\uffff\1\105\1\110\1\105\6\uffff\1\52\1\170\1\75\1\uffff"+
		"\1\75\1\uffff\1\102\2\105\1\125\1\105\1\120\1\101\1\105\1\141\1\162\1"+
		"\uffff\1\0\5\uffff\1\104\1\44\1\107\1\123\1\114\1\44\2\124\1\123\1\101"+
		"\1\122\1\114\1\123\1\131\1\123\1\104\1\120\1\117\1\103\1\111\1\124\1\117"+
		"\1\116\1\117\1\126\1\125\2\44\1\111\1\101\1\103\1\113\1\130\1\116\1\115"+
		"\1\104\1\44\1\124\1\112\2\44\1\103\1\102\1\132\1\115\1\122\1\105\1\101"+
		"\1\120\2\105\12\uffff\1\101\2\uffff\1\131\1\127\1\124\1\114\1\101\1\107"+
		"\1\104\1\105\1\114\1\101\1\154\1\165\2\0\2\uffff\3\44\1\uffff\1\44\1\50"+
		"\1\44\1\uffff\1\127\1\110\1\105\1\116\1\114\1\103\1\122\1\103\1\105\1"+
		"\124\1\44\1\105\1\44\1\122\1\124\1\103\1\101\1\123\1\122\1\103\1\115\1"+
		"\103\1\125\1\111\1\122\2\105\2\uffff\1\116\1\124\1\104\1\107\1\105\1\101"+
		"\1\105\2\44\1\102\1\50\1\124\1\105\1\uffff\2\105\2\uffff\1\44\1\117\1"+
		"\105\1\44\1\123\2\105\1\124\1\116\1\111\1\101\1\115\1\105\1\116\1\113"+
		"\1\124\1\50\3\44\1\114\1\122\1\105\1\101\1\105\1\122\1\125\1\122\1\163"+
		"\1\145\10\uffff\1\105\2\44\1\50\1\124\1\105\1\101\1\105\1\44\1\124\1\111"+
		"\1\uffff\1\44\1\uffff\2\131\1\110\1\120\1\124\1\101\1\110\1\44\1\124\1"+
		"\120\1\116\1\44\1\122\1\130\2\44\1\111\1\124\1\122\1\124\1\44\1\uffff"+
		"\1\124\1\uffff\1\105\1\uffff\1\110\2\122\1\103\1\uffff\1\116\1\103\1\uffff"+
		"\1\124\1\50\1\44\1\50\1\44\1\114\1\124\2\50\1\44\1\105\1\44\1\105\4\uffff"+
		"\1\44\1\124\1\130\1\124\1\122\1\137\1\105\1\44\1\145\1\44\1\uffff\1\105"+
		"\3\uffff\1\44\1\123\1\124\1\116\1\uffff\1\105\1\116\1\uffff\1\50\2\44"+
		"\1\105\1\123\1\103\1\44\1\uffff\1\111\1\44\1\107\1\uffff\1\44\1\50\2\uffff"+
		"\1\116\1\110\1\44\1\105\1\uffff\1\105\1\122\3\44\1\124\1\104\1\124\1\122"+
		"\4\uffff\1\111\1\50\3\uffff\1\44\1\uffff\1\101\1\106\1\40\1\uffff\1\105"+
		"\1\120\1\105\1\50\1\124\1\50\1\uffff\1\44\1\uffff\1\116\1\uffff\1\103"+
		"\1\50\1\124\1\44\1\103\3\uffff\2\44\1\124\1\uffff\1\117\1\uffff\1\44\2"+
		"\uffff\1\107\1\50\1\uffff\1\50\2\44\3\uffff\3\44\1\111\1\116\5\uffff\1"+
		"\50\1\106\1\122\2\44\1\uffff\1\111\2\uffff\1\44\1\105\1\uffff\1\137\1"+
		"\uffff\1\124\2\uffff\1\50\1\116\1\uffff\1\44\7\uffff\1\116\1\107\3\uffff"+
		"\1\44\2\uffff\1\115\1\uffff\1\50\1\104\1\44\1\uffff\1\50\1\uffff\1\107"+
		"\1\44\1\uffff\1\105\1\uffff\1\101\1\111\2\uffff\1\50\1\uffff\1\132\1\124"+
		"\1\115\1\uffff\1\117\2\105\1\116\2\44\1\105\1\uffff\1\124\1\uffff\1\44"+
		"\1\101\1\uffff\1\115\1\120\1\44\1\uffff";
	static final String DFA11_maxS =
		"\1\u052f\1\126\1\131\1\125\1\111\1\130\1\125\1\122\1\117\1\123\2\117\1"+
		"\uffff\1\117\1\125\1\uffff\1\125\1\131\1\110\6\uffff\1\57\1\170\1\76\1"+
		"\uffff\1\75\1\uffff\1\124\1\105\2\125\1\105\1\123\1\101\1\105\1\141\1"+
		"\162\1\uffff\1\uffff\5\uffff\1\131\1\172\1\107\1\123\1\114\1\172\2\124"+
		"\1\123\1\125\1\122\2\123\1\131\1\123\1\124\1\120\1\117\1\103\2\124\1\117"+
		"\1\116\1\117\1\126\1\125\2\172\1\111\1\116\1\127\1\113\1\130\1\116\1\115"+
		"\1\116\1\172\1\124\1\112\2\172\1\124\1\115\1\132\1\115\1\122\1\105\1\111"+
		"\1\120\2\105\12\uffff\1\101\2\uffff\1\131\2\127\1\114\1\101\1\107\1\120"+
		"\1\105\1\114\1\101\1\154\1\165\2\uffff\2\uffff\3\172\1\uffff\1\172\1\50"+
		"\1\172\1\uffff\1\127\1\110\1\124\1\116\1\114\1\103\1\122\1\103\1\105\1"+
		"\124\1\172\1\105\1\172\1\122\1\124\1\103\1\101\1\123\1\122\1\103\1\115"+
		"\1\103\1\125\1\111\1\122\2\105\2\uffff\1\116\1\124\1\104\1\107\1\105\1"+
		"\101\1\105\2\172\1\102\1\50\1\124\1\105\1\uffff\2\105\2\uffff\1\172\1"+
		"\117\1\105\1\172\1\123\2\105\1\124\1\116\1\111\1\101\1\115\1\105\1\122"+
		"\1\113\1\124\1\50\3\172\1\114\1\122\1\105\1\101\1\105\1\122\1\125\1\122"+
		"\1\163\1\145\10\uffff\1\105\2\172\1\50\1\124\1\105\1\101\1\105\1\172\1"+
		"\124\1\111\1\uffff\1\172\1\uffff\2\131\1\110\1\120\1\124\1\101\1\110\1"+
		"\172\1\124\1\120\1\116\1\172\1\122\1\130\2\172\1\111\1\124\1\122\1\124"+
		"\1\172\1\uffff\1\124\1\uffff\1\105\1\uffff\1\110\2\122\1\103\1\uffff\1"+
		"\116\1\103\1\uffff\1\124\1\50\1\172\1\50\1\172\1\114\1\124\2\50\1\172"+
		"\1\105\1\172\1\105\4\uffff\1\172\1\124\1\130\1\124\1\122\1\137\1\105\1"+
		"\172\1\145\1\172\1\uffff\1\105\3\uffff\1\172\1\123\1\124\1\116\1\uffff"+
		"\1\105\1\116\1\uffff\1\50\2\172\1\105\1\123\1\103\1\172\1\uffff\1\111"+
		"\1\172\1\107\1\uffff\1\172\1\50\2\uffff\1\116\1\110\1\172\1\105\1\uffff"+
		"\1\105\1\122\3\172\1\124\1\104\1\124\1\122\4\uffff\1\111\1\50\3\uffff"+
		"\1\172\1\uffff\1\105\1\106\1\40\1\uffff\1\105\1\120\1\105\1\50\1\124\1"+
		"\50\1\uffff\1\172\1\uffff\1\116\1\uffff\1\103\1\50\1\124\1\172\1\103\3"+
		"\uffff\2\172\1\124\1\uffff\1\117\1\uffff\1\172\2\uffff\1\107\1\50\1\uffff"+
		"\1\50\2\172\3\uffff\3\172\1\111\1\116\5\uffff\1\50\1\114\1\122\2\172\1"+
		"\uffff\1\111\2\uffff\1\172\1\105\1\uffff\1\137\1\uffff\1\124\2\uffff\1"+
		"\50\1\116\1\uffff\1\172\7\uffff\1\116\1\107\3\uffff\1\172\2\uffff\1\115"+
		"\1\uffff\1\50\1\124\1\172\1\uffff\1\50\1\uffff\1\107\1\172\1\uffff\1\105"+
		"\1\uffff\1\101\1\111\2\uffff\1\50\1\uffff\1\132\1\124\1\115\1\uffff\1"+
		"\117\2\105\1\116\2\172\1\105\1\uffff\1\124\1\uffff\1\172\1\101\1\uffff"+
		"\1\115\1\120\1\172\1\uffff";
	static final String DFA11_acceptS =
		"\14\uffff\1\23\2\uffff\1\31\3\uffff\1\36\1\37\1\40\1\41\1\42\1\43\3\uffff"+
		"\1\51\1\uffff\1\54\12\uffff\1\162\1\uffff\1\167\1\170\1\171\1\172\1\176"+
		"\63\uffff\1\173\1\174\1\44\1\45\1\47\1\50\1\46\1\53\1\52\1\55\1\uffff"+
		"\1\61\1\62\16\uffff\1\175\1\166\3\uffff\1\2\3\uffff\1\5\33\uffff\1\164"+
		"\1\112\15\uffff\1\26\2\uffff\1\133\1\134\36\uffff\1\165\1\166\1\1\1\65"+
		"\1\3\1\4\1\63\1\64\13\uffff\1\76\1\uffff\1\13\25\uffff\1\24\1\uffff\1"+
		"\25\1\uffff\1\122\4\uffff\1\32\2\uffff\1\33\15\uffff\1\113\1\124\1\125"+
		"\1\163\12\uffff\1\165\1\uffff\1\67\1\6\1\70\4\uffff\1\10\2\uffff\1\12"+
		"\7\uffff\1\106\3\uffff\1\110\2\uffff\1\20\1\21\4\uffff\1\116\11\uffff"+
		"\1\141\1\142\1\143\1\34\2\uffff\1\147\1\150\1\35\1\uffff\1\155\3\uffff"+
		"\1\126\6\uffff\1\157\1\uffff\1\161\1\uffff\1\7\5\uffff\1\101\1\100\1\102"+
		"\3\uffff\1\14\1\uffff\1\15\1\uffff\1\17\1\111\2\uffff\1\22\3\uffff\1\123"+
		"\1\27\1\30\5\uffff\1\146\1\156\1\56\1\57\1\60\5\uffff\1\152\1\uffff\1"+
		"\154\1\160\2\uffff\1\72\1\uffff\1\77\1\uffff\1\103\1\104\2\uffff\1\16"+
		"\1\uffff\1\115\1\117\1\121\1\120\1\132\1\137\1\140\2\uffff\1\127\1\130"+
		"\1\131\1\uffff\1\136\1\151\1\uffff\1\66\3\uffff\1\105\1\uffff\1\114\2"+
		"\uffff\1\135\1\uffff\1\71\2\uffff\1\11\1\107\1\uffff\1\145\3\uffff\1\144"+
		"\7\uffff\1\73\1\uffff\1\74\2\uffff\1\153\3\uffff\1\75";
	static final String DFA11_specialS =
		"\52\uffff\1\1\121\uffff\1\0\1\2\u015f\uffff}>";
	static final String[] DFA11_transitionS = {
			"\2\56\1\uffff\2\56\22\uffff\1\56\3\uffff\1\23\2\uffff\1\52\1\14\1\17"+
			"\1\24\1\25\1\26\1\27\1\30\1\31\1\32\11\57\1\55\1\uffff\1\33\1\34\1\35"+
			"\1\36\1\37\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\40\1\13\1\15"+
			"\1\41\1\16\1\53\1\42\1\43\1\20\1\21\1\44\1\45\1\22\1\53\1\46\1\53\4\uffff"+
			"\1\53\1\uffff\5\53\1\47\15\53\1\50\6\53\2\uffff\1\51\u0382\uffff\u0130"+
			"\54",
			"\1\63\11\uffff\1\64\1\uffff\1\60\4\uffff\1\61\2\uffff\1\62",
			"\1\66\11\uffff\1\67\11\uffff\1\65",
			"\1\70\15\uffff\1\71\5\uffff\1\72",
			"\1\75\3\uffff\1\73\3\uffff\1\74",
			"\1\76\1\100\1\77\1\uffff\1\101\2\uffff\1\102\4\uffff\1\103",
			"\1\104\14\uffff\1\105\2\uffff\1\106",
			"\1\107",
			"\1\110\15\uffff\1\111",
			"\1\112\4\uffff\1\113",
			"\1\114",
			"\1\115\3\uffff\1\117\5\uffff\1\116",
			"",
			"\1\120\3\uffff\1\122\3\uffff\1\121\5\uffff\1\123",
			"\1\126\3\uffff\1\127\7\uffff\1\130\3\uffff\1\124\2\uffff\1\125",
			"",
			"\1\131\3\uffff\1\133\5\uffff\1\134\1\uffff\1\135\3\uffff\1\132",
			"\1\136\11\uffff\1\137\6\uffff\1\140",
			"\1\142\2\uffff\1\141",
			"",
			"",
			"",
			"",
			"",
			"",
			"\1\143\4\uffff\1\144",
			"\1\146",
			"\1\147\1\150",
			"",
			"\1\152",
			"",
			"\1\154\1\uffff\1\155\1\156\16\uffff\1\157",
			"\1\160",
			"\1\161\11\uffff\1\162\5\uffff\1\163",
			"\1\164",
			"\1\165",
			"\1\166\2\uffff\1\167",
			"\1\170",
			"\1\171",
			"\1\172",
			"\1\173",
			"",
			"\42\175\1\176\4\175\1\177\6\175\1\174\55\175\1\177\uffa3\175",
			"",
			"",
			"",
			"",
			"",
			"\1\u0080\24\uffff\1\u0081",
			"\1\53\13\uffff\12\53\7\uffff\2\53\1\u0082\27\53\4\uffff\1\53\1\uffff"+
			"\32\53",
			"\1\u0084",
			"\1\u0085",
			"\1\u0086",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			"\1\u0088",
			"\1\u0089",
			"\1\u008a",
			"\1\u008c\14\uffff\1\u008d\6\uffff\1\u008b",
			"\1\u008e",
			"\1\u0090\6\uffff\1\u008f",
			"\1\u0091",
			"\1\u0092",
			"\1\u0093",
			"\1\u0094\17\uffff\1\u0095",
			"\1\u0096",
			"\1\u0097",
			"\1\u0098",
			"\1\u0099\12\uffff\1\u009a",
			"\1\u009b",
			"\1\u009c",
			"\1\u009d",
			"\1\u009e",
			"\1\u009f",
			"\1\u00a0",
			"\1\53\13\uffff\12\53\7\uffff\3\53\1\u00a2\11\53\1\u00a1\14\53\4\uffff"+
			"\1\53\1\uffff\32\53",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			"\1\u00a5",
			"\1\u00a7\4\uffff\1\u00a6\7\uffff\1\u00a8",
			"\1\u00aa\23\uffff\1\u00a9",
			"\1\u00ab",
			"\1\u00ac",
			"\1\u00ad",
			"\1\u00ae",
			"\1\u00af\11\uffff\1\u00b0",
			"\1\53\13\uffff\12\53\7\uffff\3\53\1\u00b1\26\53\4\uffff\1\53\1\uffff"+
			"\32\53",
			"\1\u00b3",
			"\1\u00b4",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			"\1\u00b8\10\uffff\1\u00b9\7\uffff\1\u00b7",
			"\1\u00bb\12\uffff\1\u00ba",
			"\1\u00bc",
			"\1\u00bd",
			"\1\u00be",
			"\1\u00bf",
			"\1\u00c0\3\uffff\1\u00c1\3\uffff\1\u00c2",
			"\1\u00c3",
			"\1\u00c4",
			"\1\u00c5",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"\1\u00c6",
			"",
			"",
			"\1\u00c7",
			"\1\u00c8",
			"\1\u00ca\2\uffff\1\u00c9",
			"\1\u00cb",
			"\1\u00cc",
			"\1\u00cd",
			"\1\u00ce\13\uffff\1\u00cf",
			"\1\u00d0",
			"\1\u00d1",
			"\1\u00d2",
			"\1\u00d3",
			"\1\u00d4",
			"\42\177\1\uffff\4\177\1\u00d5\uffd8\177",
			"\42\177\1\uffff\4\177\1\u00d6\uffd8\177",
			"",
			"",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			"",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			"\1\u00db",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			"",
			"\1\u00dd",
			"\1\u00de",
			"\1\u00df\16\uffff\1\u00e0",
			"\1\u00e1",
			"\1\u00e2",
			"\1\u00e3",
			"\1\u00e4",
			"\1\u00e5",
			"\1\u00e6",
			"\1\u00e7",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			"\1\u00e9",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			"\1\u00eb",
			"\1\u00ec",
			"\1\u00ed",
			"\1\u00ee",
			"\1\u00ef",
			"\1\u00f0",
			"\1\u00f1",
			"\1\u00f2",
			"\1\u00f3",
			"\1\u00f4",
			"\1\u00f5",
			"\1\u00f6",
			"\1\u00f7",
			"\1\u00f8",
			"",
			"",
			"\1\u00f9",
			"\1\u00fa",
			"\1\u00fb",
			"\1\u00fc",
			"\1\u00fd",
			"\1\u00fe",
			"\1\u00ff",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			"\1\53\13\uffff\12\53\7\uffff\24\53\1\u0101\5\53\4\uffff\1\53\1\uffff"+
			"\32\53",
			"\1\u0103",
			"\1\u0104",
			"\1\u0105",
			"\1\u0106",
			"",
			"\1\u0107",
			"\1\u0108",
			"",
			"",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			"\1\u010a",
			"\1\u010b",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			"\1\u010d",
			"\1\u010e",
			"\1\u010f",
			"\1\u0110",
			"\1\u0111",
			"\1\u0112",
			"\1\u0113",
			"\1\u0114",
			"\1\u0115",
			"\1\u0116\3\uffff\1\u0117",
			"\1\u0118",
			"\1\u0119",
			"\1\u011a",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			"\1\u011e",
			"\1\u011f",
			"\1\u0120",
			"\1\u0121",
			"\1\u0122",
			"\1\u0123",
			"\1\u0124",
			"\1\u0125",
			"\1\u0126",
			"\1\u0127",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"\1\u0129",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			"\1\u012c",
			"\1\u012d",
			"\1\u012e",
			"\1\u012f",
			"\1\u0130",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			"\1\u0132",
			"\1\u0133",
			"",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			"",
			"\1\u0135",
			"\1\u0136",
			"\1\u0137",
			"\1\u0138",
			"\1\u0139",
			"\1\u013a",
			"\1\u013b",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			"\1\u013d",
			"\1\u013e",
			"\1\u013f",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			"\1\u0141",
			"\1\u0142",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			"\1\u0145",
			"\1\u0146",
			"\1\u0147",
			"\1\u0148",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			"",
			"\1\u014a",
			"",
			"\1\u014b",
			"",
			"\1\u014c",
			"\1\u014d",
			"\1\u014e",
			"\1\u014f",
			"",
			"\1\u0150",
			"\1\u0151",
			"",
			"\1\u0152",
			"\1\u0153",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			"\1\u0155",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			"\1\u0157",
			"\1\u0158",
			"\1\u0159",
			"\1\u015a",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			"\1\u015c",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			"\1\u015e",
			"",
			"",
			"",
			"",
			"\1\53\13\uffff\12\53\7\uffff\10\53\1\u015f\11\53\1\u0160\7\53\4\uffff"+
			"\1\53\1\uffff\32\53",
			"\1\u0162",
			"\1\u0163",
			"\1\u0164",
			"\1\u0165",
			"\1\u0166",
			"\1\u0167",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			"\1\u0169",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			"",
			"\1\u016b",
			"",
			"",
			"",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			"\1\u016d",
			"\1\u016e",
			"\1\u016f",
			"",
			"\1\u0170",
			"\1\u0171",
			"",
			"\1\u0172",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			"\1\u0175",
			"\1\u0176",
			"\1\u0177",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			"",
			"\1\u0179",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			"\1\u017b",
			"",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			"\1\u017d",
			"",
			"",
			"\1\u017e",
			"\1\u017f",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			"\1\u0181",
			"",
			"\1\u0182",
			"\1\u0183",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			"\1\u0187",
			"\1\u0188",
			"\1\u0189",
			"\1\u018a",
			"",
			"",
			"",
			"",
			"\1\u018b",
			"\1\u018c",
			"",
			"",
			"",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			"",
			"\1\u018e\1\u018f\2\uffff\1\u0190",
			"\1\u0191",
			"\1\u0192",
			"",
			"\1\u0193",
			"\1\u0194",
			"\1\u0195",
			"\1\u0196",
			"\1\u0197",
			"\1\u0198",
			"",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			"",
			"\1\u019a",
			"",
			"\1\u019b",
			"\1\u019c",
			"\1\u019d",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			"\1\u019f",
			"",
			"",
			"",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			"\1\u01a2",
			"",
			"\1\u01a3",
			"",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			"",
			"",
			"\1\u01a5",
			"\1\u01a6",
			"",
			"\1\u01a7",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			"",
			"",
			"",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			"\1\u01ad",
			"\1\u01ae",
			"",
			"",
			"",
			"",
			"",
			"\1\u01af",
			"\1\u01b0\5\uffff\1\u01b1",
			"\1\u01b2",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			"",
			"\1\u01b5",
			"",
			"",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			"\1\u01b7",
			"",
			"\1\u01b8",
			"",
			"\1\u01b9",
			"",
			"",
			"\1\u01ba",
			"\1\u01bb",
			"",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"\1\u01bd",
			"\1\u01be",
			"",
			"",
			"",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			"",
			"",
			"\1\u01c0",
			"",
			"\1\u01c1",
			"\1\u01c2\17\uffff\1\u01c3",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			"",
			"\1\u01c5",
			"",
			"\1\u01c6",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			"",
			"\1\u01c8",
			"",
			"\1\u01c9",
			"\1\u01ca",
			"",
			"",
			"\1\u01cb",
			"",
			"\1\u01cc",
			"\1\u01cd",
			"\1\u01ce",
			"",
			"\1\u01cf",
			"\1\u01d0",
			"\1\u01d1",
			"\1\u01d2",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			"\1\53\13\uffff\12\53\7\uffff\22\53\1\u01d4\7\53\4\uffff\1\53\1\uffff"+
			"\32\53",
			"\1\u01d6",
			"",
			"\1\u01d7",
			"",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			"\1\u01d9",
			"",
			"\1\u01da",
			"\1\u01db",
			"\1\53\13\uffff\12\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32\53",
			""
	};

	static final short[] DFA11_eot = DFA.unpackEncodedString(DFA11_eotS);
	static final short[] DFA11_eof = DFA.unpackEncodedString(DFA11_eofS);
	static final char[] DFA11_min = DFA.unpackEncodedStringToUnsignedChars(DFA11_minS);
	static final char[] DFA11_max = DFA.unpackEncodedStringToUnsignedChars(DFA11_maxS);
	static final short[] DFA11_accept = DFA.unpackEncodedString(DFA11_acceptS);
	static final short[] DFA11_special = DFA.unpackEncodedString(DFA11_specialS);
	static final short[][] DFA11_transition;

	static {
		int numStates = DFA11_transitionS.length;
		DFA11_transition = new short[numStates][];
		for (int i=0; i<numStates; i++) {
			DFA11_transition[i] = DFA.unpackEncodedString(DFA11_transitionS[i]);
		}
	}

	protected class DFA11 extends DFA {

		public DFA11(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 11;
			this.eot = DFA11_eot;
			this.eof = DFA11_eof;
			this.min = DFA11_min;
			this.max = DFA11_max;
			this.accept = DFA11_accept;
			this.special = DFA11_special;
			this.transition = DFA11_transition;
		}
		@Override
		public String getDescription() {
			return "1:1: Tokens : ( AND | AS | ASC | AVG | BY | CASE | COUNT | DESC | DISTINCT | ELSE | END | FETCH | GROUP | HAVING | INNER | JOIN | LEFT | LOWER | LPAREN | MAX | MIN | OR | ORDER | OUTER | RPAREN | SET | SUM | THEN | WHEN | T__63 | T__64 | T__65 | T__66 | T__67 | T__68 | T__69 | T__70 | T__71 | T__72 | T__73 | T__74 | T__75 | T__76 | T__77 | T__78 | T__79 | T__80 | T__81 | T__82 | T__83 | T__84 | T__85 | T__86 | T__87 | T__88 | T__89 | T__90 | T__91 | T__92 | T__93 | T__94 | T__95 | T__96 | T__97 | T__98 | T__99 | T__100 | T__101 | T__102 | T__103 | T__104 | T__105 | T__106 | T__107 | T__108 | T__109 | T__110 | T__111 | T__112 | T__113 | T__114 | T__115 | T__116 | T__117 | T__118 | T__119 | T__120 | T__121 | T__122 | T__123 | T__124 | T__125 | T__126 | T__127 | T__128 | T__129 | T__130 | T__131 | T__132 | T__133 | T__134 | T__135 | T__136 | T__137 | T__138 | T__139 | T__140 | T__141 | T__142 | T__143 | T__144 | T__145 | T__146 | T__147 | NOT | IN | TRIM_CHARACTER | STRING_LITERAL | WORD | RUSSIAN_SYMBOLS | NAMED_PARAMETER | WS | COMMENT | LINE_COMMENT | ESCAPE_CHARACTER | INT_NUMERAL );";
		}
		@Override
		public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
			IntStream input = _input;
			int _s = s;
			switch ( s ) {
					case 0 : 
						int LA11_124 = input.LA(1);
						s = -1;
						if ( (LA11_124=='\'') ) {s = 213;}
						else if ( ((LA11_124 >= '\u0000' && LA11_124 <= '!')||(LA11_124 >= '#' && LA11_124 <= '&')||(LA11_124 >= '(' && LA11_124 <= '\uFFFF')) ) {s = 127;}
						if ( s>=0 ) return s;
						break;

					case 1 : 
						int LA11_42 = input.LA(1);
						s = -1;
						if ( (LA11_42=='.') ) {s = 124;}
						else if ( ((LA11_42 >= '\u0000' && LA11_42 <= '!')||(LA11_42 >= '#' && LA11_42 <= '&')||(LA11_42 >= '(' && LA11_42 <= '-')||(LA11_42 >= '/' && LA11_42 <= '[')||(LA11_42 >= ']' && LA11_42 <= '\uFFFF')) ) {s = 125;}
						else if ( (LA11_42=='\"') ) {s = 126;}
						else if ( (LA11_42=='\''||LA11_42=='\\') ) {s = 127;}
						if ( s>=0 ) return s;
						break;

					case 2 : 
						int LA11_125 = input.LA(1);
						s = -1;
						if ( (LA11_125=='\'') ) {s = 214;}
						else if ( ((LA11_125 >= '\u0000' && LA11_125 <= '!')||(LA11_125 >= '#' && LA11_125 <= '&')||(LA11_125 >= '(' && LA11_125 <= '\uFFFF')) ) {s = 127;}
						if ( s>=0 ) return s;
						break;
			}
			NoViableAltException nvae =
				new NoViableAltException(getDescription(), 11, _s, input);
			error(nvae);
			throw nvae;
		}
	}

	@Override
	public void emitErrorMessage(String msg) {
		throw new JPA2RecognitionException(msg);
	}
}
