/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

// $ANTLR 3.2 Sep 23, 2009 12:02:23 JPA.g 2012-08-06 14:42:50

package com.haulmont.cuba.core.sys.jpql.antlr;



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class JPALexer extends Lexer {
    public static final int T_JOIN_VAR=11;
    public static final int T_AGGREGATE_EXPR=20;
    public static final int COUNT=28;
    public static final int T_ORDER_BY=18;
    public static final int EOF=-1;
    public static final int WORD=46;
    public static final int T__93=93;
    public static final int T__94=94;
    public static final int RPAREN=32;
    public static final int T__91=91;
    public static final int T__92=92;
    public static final int T__90=90;
    public static final int TRIM_CHARACTER=45;
    public static final int T_SELECTED_ITEM=5;
    public static final int COMMENT=50;
    public static final int T__99=99;
    public static final int T__98=98;
    public static final int T__97=97;
    public static final int T_QUERY=13;
    public static final int T__96=96;
    public static final int T__95=95;
    public static final int T__80=80;
    public static final int T__81=81;
    public static final int T__82=82;
    public static final int ASC=22;
    public static final int T__83=83;
    public static final int LINE_COMMENT=51;
    public static final int T__85=85;
    public static final int T__84=84;
    public static final int T__87=87;
    public static final int T__86=86;
    public static final int T__89=89;
    public static final int T__88=88;
    public static final int GROUP=40;
    public static final int T__71=71;
    public static final int WS=49;
    public static final int T__72=72;
    public static final int FETCH=38;
    public static final int T__70=70;
    public static final int T_SELECTED_FIELD=8;
    public static final int OR=29;
    public static final int T__76=76;
    public static final int T__75=75;
    public static final int DISTINCT=33;
    public static final int T__74=74;
    public static final int T__73=73;
    public static final int T__79=79;
    public static final int T__78=78;
    public static final int T__77=77;
    public static final int T__68=68;
    public static final int T__69=69;
    public static final int T__66=66;
    public static final int T__67=67;
    public static final int T_SELECTED_ITEMS=4;
    public static final int T__64=64;
    public static final int T__65=65;
    public static final int INNER=36;
    public static final int T__62=62;
    public static final int T__63=63;
    public static final int ORDER=39;
    public static final int T__118=118;
    public static final int T_SOURCE=7;
    public static final int T__119=119;
    public static final int T__116=116;
    public static final int T_ID_VAR=10;
    public static final int T__117=117;
    public static final int T_SIMPLE_CONDITION=15;
    public static final int T__114=114;
    public static final int T__115=115;
    public static final int MAX=25;
    public static final int AND=30;
    public static final int SUM=27;
    public static final int T__61=61;
    public static final int T__60=60;
    public static final int RUSSIAN_SYMBOLS=48;
    public static final int LPAREN=31;
    public static final int T__55=55;
    public static final int T__56=56;
    public static final int T__57=57;
    public static final int T__58=58;
    public static final int T__52=52;
    public static final int T__53=53;
    public static final int T__54=54;
    public static final int T__107=107;
    public static final int T__108=108;
    public static final int T__109=109;
    public static final int LEFT=34;
    public static final int AVG=24;
    public static final int T_ORDER_BY_FIELD=19;
    public static final int T__103=103;
    public static final int T__59=59;
    public static final int T__104=104;
    public static final int T__105=105;
    public static final int T__106=106;
    public static final int T__111=111;
    public static final int T__110=110;
    public static final int T__113=113;
    public static final int T__112=112;
    public static final int T_GROUP_BY=17;
    public static final int OUTER=35;
    public static final int BY=41;
    public static final int T_CONDITION=14;
    public static final int T_SELECTED_ENTITY=9;
    public static final int HAVING=21;
    public static final int T__102=102;
    public static final int T__101=101;
    public static final int T__100=100;
    public static final int MIN=26;
    public static final int T_PARAMETER=16;
    public static final int JOIN=37;
    public static final int ESCAPE_CHARACTER=43;
    public static final int NAMED_PARAMETER=47;
    public static final int INT_NUMERAL=42;
    public static final int STRINGLITERAL=44;
    public static final int T_COLLECTION_MEMBER=12;
    public static final int DESC=23;
    public static final int T_SOURCES=6;

    // delegates
    // delegators

    public JPALexer() {;} 
    public JPALexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public JPALexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "JPA.g"; }

    // $ANTLR start "HAVING"
    public final void mHAVING() throws RecognitionException {
        try {
            int _type = HAVING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:8:8: ( 'HAVING' )
            // JPA.g:8:10: 'HAVING'
            {
            match("HAVING"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "HAVING"

    // $ANTLR start "ASC"
    public final void mASC() throws RecognitionException {
        try {
            int _type = ASC;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:9:5: ( 'ASC' )
            // JPA.g:9:7: 'ASC'
            {
            match("ASC"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ASC"

    // $ANTLR start "DESC"
    public final void mDESC() throws RecognitionException {
        try {
            int _type = DESC;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:10:6: ( 'DESC' )
            // JPA.g:10:8: 'DESC'
            {
            match("DESC"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DESC"

    // $ANTLR start "AVG"
    public final void mAVG() throws RecognitionException {
        try {
            int _type = AVG;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:11:5: ( 'AVG' )
            // JPA.g:11:7: 'AVG'
            {
            match("AVG"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "AVG"

    // $ANTLR start "MAX"
    public final void mMAX() throws RecognitionException {
        try {
            int _type = MAX;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:12:5: ( 'MAX' )
            // JPA.g:12:7: 'MAX'
            {
            match("MAX"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "MAX"

    // $ANTLR start "MIN"
    public final void mMIN() throws RecognitionException {
        try {
            int _type = MIN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:13:5: ( 'MIN' )
            // JPA.g:13:7: 'MIN'
            {
            match("MIN"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "MIN"

    // $ANTLR start "SUM"
    public final void mSUM() throws RecognitionException {
        try {
            int _type = SUM;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:14:5: ( 'SUM' )
            // JPA.g:14:7: 'SUM'
            {
            match("SUM"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SUM"

    // $ANTLR start "COUNT"
    public final void mCOUNT() throws RecognitionException {
        try {
            int _type = COUNT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:15:7: ( 'COUNT' )
            // JPA.g:15:9: 'COUNT'
            {
            match("COUNT"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "COUNT"

    // $ANTLR start "OR"
    public final void mOR() throws RecognitionException {
        try {
            int _type = OR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:16:4: ( 'OR' )
            // JPA.g:16:6: 'OR'
            {
            match("OR"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "OR"

    // $ANTLR start "AND"
    public final void mAND() throws RecognitionException {
        try {
            int _type = AND;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:17:5: ( 'AND' )
            // JPA.g:17:7: 'AND'
            {
            match("AND"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "AND"

    // $ANTLR start "LPAREN"
    public final void mLPAREN() throws RecognitionException {
        try {
            int _type = LPAREN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:18:8: ( '(' )
            // JPA.g:18:10: '('
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LPAREN"

    // $ANTLR start "RPAREN"
    public final void mRPAREN() throws RecognitionException {
        try {
            int _type = RPAREN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:19:8: ( ')' )
            // JPA.g:19:10: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RPAREN"

    // $ANTLR start "DISTINCT"
    public final void mDISTINCT() throws RecognitionException {
        try {
            int _type = DISTINCT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:20:10: ( 'DISTINCT' )
            // JPA.g:20:12: 'DISTINCT'
            {
            match("DISTINCT"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DISTINCT"

    // $ANTLR start "LEFT"
    public final void mLEFT() throws RecognitionException {
        try {
            int _type = LEFT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:21:6: ( 'LEFT' )
            // JPA.g:21:8: 'LEFT'
            {
            match("LEFT"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LEFT"

    // $ANTLR start "OUTER"
    public final void mOUTER() throws RecognitionException {
        try {
            int _type = OUTER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:22:7: ( 'OUTER' )
            // JPA.g:22:9: 'OUTER'
            {
            match("OUTER"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "OUTER"

    // $ANTLR start "INNER"
    public final void mINNER() throws RecognitionException {
        try {
            int _type = INNER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:23:7: ( 'INNER' )
            // JPA.g:23:9: 'INNER'
            {
            match("INNER"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INNER"

    // $ANTLR start "JOIN"
    public final void mJOIN() throws RecognitionException {
        try {
            int _type = JOIN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:24:6: ( 'JOIN' )
            // JPA.g:24:8: 'JOIN'
            {
            match("JOIN"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "JOIN"

    // $ANTLR start "FETCH"
    public final void mFETCH() throws RecognitionException {
        try {
            int _type = FETCH;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:25:7: ( 'FETCH' )
            // JPA.g:25:9: 'FETCH'
            {
            match("FETCH"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "FETCH"

    // $ANTLR start "ORDER"
    public final void mORDER() throws RecognitionException {
        try {
            int _type = ORDER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:26:7: ( 'ORDER' )
            // JPA.g:26:9: 'ORDER'
            {
            match("ORDER"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ORDER"

    // $ANTLR start "GROUP"
    public final void mGROUP() throws RecognitionException {
        try {
            int _type = GROUP;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:27:7: ( 'GROUP' )
            // JPA.g:27:9: 'GROUP'
            {
            match("GROUP"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "GROUP"

    // $ANTLR start "BY"
    public final void mBY() throws RecognitionException {
        try {
            int _type = BY;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:28:4: ( 'BY' )
            // JPA.g:28:6: 'BY'
            {
            match("BY"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "BY"

    // $ANTLR start "T__52"
    public final void mT__52() throws RecognitionException {
        try {
            int _type = T__52;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:29:7: ( 'SELECT' )
            // JPA.g:29:9: 'SELECT'
            {
            match("SELECT"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__52"

    // $ANTLR start "T__53"
    public final void mT__53() throws RecognitionException {
        try {
            int _type = T__53;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:30:7: ( 'FROM' )
            // JPA.g:30:9: 'FROM'
            {
            match("FROM"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__53"

    // $ANTLR start "T__54"
    public final void mT__54() throws RecognitionException {
        try {
            int _type = T__54;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:31:7: ( ',' )
            // JPA.g:31:9: ','
            {
            match(','); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__54"

    // $ANTLR start "T__55"
    public final void mT__55() throws RecognitionException {
        try {
            int _type = T__55;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:32:7: ( 'AS' )
            // JPA.g:32:9: 'AS'
            {
            match("AS"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__55"

    // $ANTLR start "T__56"
    public final void mT__56() throws RecognitionException {
        try {
            int _type = T__56;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:33:7: ( '(SELECT' )
            // JPA.g:33:9: '(SELECT'
            {
            match("(SELECT"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__56"

    // $ANTLR start "T__57"
    public final void mT__57() throws RecognitionException {
        try {
            int _type = T__57;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:34:7: ( '.' )
            // JPA.g:34:9: '.'
            {
            match('.'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__57"

    // $ANTLR start "T__58"
    public final void mT__58() throws RecognitionException {
        try {
            int _type = T__58;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:35:7: ( 'IN' )
            // JPA.g:35:9: 'IN'
            {
            match("IN"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__58"

    // $ANTLR start "T__59"
    public final void mT__59() throws RecognitionException {
        try {
            int _type = T__59;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:36:7: ( 'OBJECT' )
            // JPA.g:36:9: 'OBJECT'
            {
            match("OBJECT"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__59"

    // $ANTLR start "T__60"
    public final void mT__60() throws RecognitionException {
        try {
            int _type = T__60;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:37:7: ( 'NEW' )
            // JPA.g:37:9: 'NEW'
            {
            match("NEW"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__60"

    // $ANTLR start "T__61"
    public final void mT__61() throws RecognitionException {
        try {
            int _type = T__61;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:38:7: ( 'WHERE' )
            // JPA.g:38:9: 'WHERE'
            {
            match("WHERE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__61"

    // $ANTLR start "T__62"
    public final void mT__62() throws RecognitionException {
        try {
            int _type = T__62;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:39:7: ( 'NOT' )
            // JPA.g:39:9: 'NOT'
            {
            match("NOT"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__62"

    // $ANTLR start "T__63"
    public final void mT__63() throws RecognitionException {
        try {
            int _type = T__63;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:40:7: ( '@BETWEEN' )
            // JPA.g:40:9: '@BETWEEN'
            {
            match("@BETWEEN"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__63"

    // $ANTLR start "T__64"
    public final void mT__64() throws RecognitionException {
        try {
            int _type = T__64;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:41:7: ( 'NOW' )
            // JPA.g:41:9: 'NOW'
            {
            match("NOW"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__64"

    // $ANTLR start "T__65"
    public final void mT__65() throws RecognitionException {
        try {
            int _type = T__65;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:42:7: ( '+' )
            // JPA.g:42:9: '+'
            {
            match('+'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__65"

    // $ANTLR start "T__66"
    public final void mT__66() throws RecognitionException {
        try {
            int _type = T__66;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:43:7: ( '-' )
            // JPA.g:43:9: '-'
            {
            match('-'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__66"

    // $ANTLR start "T__67"
    public final void mT__67() throws RecognitionException {
        try {
            int _type = T__67;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:44:7: ( 'YEAR' )
            // JPA.g:44:9: 'YEAR'
            {
            match("YEAR"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__67"

    // $ANTLR start "T__68"
    public final void mT__68() throws RecognitionException {
        try {
            int _type = T__68;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:45:7: ( 'MONTH' )
            // JPA.g:45:9: 'MONTH'
            {
            match("MONTH"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__68"

    // $ANTLR start "T__69"
    public final void mT__69() throws RecognitionException {
        try {
            int _type = T__69;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:46:7: ( 'DAY' )
            // JPA.g:46:9: 'DAY'
            {
            match("DAY"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__69"

    // $ANTLR start "T__70"
    public final void mT__70() throws RecognitionException {
        try {
            int _type = T__70;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:47:7: ( 'HOUR' )
            // JPA.g:47:9: 'HOUR'
            {
            match("HOUR"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__70"

    // $ANTLR start "T__71"
    public final void mT__71() throws RecognitionException {
        try {
            int _type = T__71;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:48:7: ( 'MINUTE' )
            // JPA.g:48:9: 'MINUTE'
            {
            match("MINUTE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__71"

    // $ANTLR start "T__72"
    public final void mT__72() throws RecognitionException {
        try {
            int _type = T__72;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:49:7: ( 'SECOND' )
            // JPA.g:49:9: 'SECOND'
            {
            match("SECOND"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__72"

    // $ANTLR start "T__73"
    public final void mT__73() throws RecognitionException {
        try {
            int _type = T__73;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:50:7: ( '@DATEBEFORE' )
            // JPA.g:50:9: '@DATEBEFORE'
            {
            match("@DATEBEFORE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__73"

    // $ANTLR start "T__74"
    public final void mT__74() throws RecognitionException {
        try {
            int _type = T__74;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:51:7: ( '@DATEAFTER' )
            // JPA.g:51:9: '@DATEAFTER'
            {
            match("@DATEAFTER"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__74"

    // $ANTLR start "T__75"
    public final void mT__75() throws RecognitionException {
        try {
            int _type = T__75;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:52:7: ( '@DATEEQUALS' )
            // JPA.g:52:9: '@DATEEQUALS'
            {
            match("@DATEEQUALS"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__75"

    // $ANTLR start "T__76"
    public final void mT__76() throws RecognitionException {
        try {
            int _type = T__76;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:53:7: ( '@TODAY' )
            // JPA.g:53:9: '@TODAY'
            {
            match("@TODAY"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__76"

    // $ANTLR start "T__77"
    public final void mT__77() throws RecognitionException {
        try {
            int _type = T__77;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:54:7: ( 'BETWEEN' )
            // JPA.g:54:9: 'BETWEEN'
            {
            match("BETWEEN"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__77"

    // $ANTLR start "T__78"
    public final void mT__78() throws RecognitionException {
        try {
            int _type = T__78;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:55:7: ( 'LIKE' )
            // JPA.g:55:9: 'LIKE'
            {
            match("LIKE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__78"

    // $ANTLR start "T__79"
    public final void mT__79() throws RecognitionException {
        try {
            int _type = T__79;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:56:7: ( 'ESCAPE' )
            // JPA.g:56:9: 'ESCAPE'
            {
            match("ESCAPE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__79"

    // $ANTLR start "T__80"
    public final void mT__80() throws RecognitionException {
        try {
            int _type = T__80;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:57:7: ( 'IS' )
            // JPA.g:57:9: 'IS'
            {
            match("IS"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__80"

    // $ANTLR start "T__81"
    public final void mT__81() throws RecognitionException {
        try {
            int _type = T__81;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:58:7: ( 'NULL' )
            // JPA.g:58:9: 'NULL'
            {
            match("NULL"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__81"

    // $ANTLR start "T__82"
    public final void mT__82() throws RecognitionException {
        try {
            int _type = T__82;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:59:7: ( 'EMPTY' )
            // JPA.g:59:9: 'EMPTY'
            {
            match("EMPTY"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__82"

    // $ANTLR start "T__83"
    public final void mT__83() throws RecognitionException {
        try {
            int _type = T__83;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:60:7: ( 'MEMBER' )
            // JPA.g:60:9: 'MEMBER'
            {
            match("MEMBER"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__83"

    // $ANTLR start "T__84"
    public final void mT__84() throws RecognitionException {
        try {
            int _type = T__84;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:61:7: ( 'OF' )
            // JPA.g:61:9: 'OF'
            {
            match("OF"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__84"

    // $ANTLR start "T__85"
    public final void mT__85() throws RecognitionException {
        try {
            int _type = T__85;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:62:7: ( 'EXISTS' )
            // JPA.g:62:9: 'EXISTS'
            {
            match("EXISTS"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__85"

    // $ANTLR start "T__86"
    public final void mT__86() throws RecognitionException {
        try {
            int _type = T__86;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:63:7: ( 'ALL' )
            // JPA.g:63:9: 'ALL'
            {
            match("ALL"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__86"

    // $ANTLR start "T__87"
    public final void mT__87() throws RecognitionException {
        try {
            int _type = T__87;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:64:7: ( 'ANY' )
            // JPA.g:64:9: 'ANY'
            {
            match("ANY"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__87"

    // $ANTLR start "T__88"
    public final void mT__88() throws RecognitionException {
        try {
            int _type = T__88;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:65:7: ( 'SOME' )
            // JPA.g:65:9: 'SOME'
            {
            match("SOME"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__88"

    // $ANTLR start "T__89"
    public final void mT__89() throws RecognitionException {
        try {
            int _type = T__89;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:66:7: ( '=' )
            // JPA.g:66:9: '='
            {
            match('='); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__89"

    // $ANTLR start "T__90"
    public final void mT__90() throws RecognitionException {
        try {
            int _type = T__90;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:67:7: ( '<>' )
            // JPA.g:67:9: '<>'
            {
            match("<>"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__90"

    // $ANTLR start "T__91"
    public final void mT__91() throws RecognitionException {
        try {
            int _type = T__91;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:68:7: ( '>' )
            // JPA.g:68:9: '>'
            {
            match('>'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__91"

    // $ANTLR start "T__92"
    public final void mT__92() throws RecognitionException {
        try {
            int _type = T__92;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:69:7: ( '>=' )
            // JPA.g:69:9: '>='
            {
            match(">="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__92"

    // $ANTLR start "T__93"
    public final void mT__93() throws RecognitionException {
        try {
            int _type = T__93;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:70:7: ( '<' )
            // JPA.g:70:9: '<'
            {
            match('<'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__93"

    // $ANTLR start "T__94"
    public final void mT__94() throws RecognitionException {
        try {
            int _type = T__94;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:71:7: ( '<=' )
            // JPA.g:71:9: '<='
            {
            match("<="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__94"

    // $ANTLR start "T__95"
    public final void mT__95() throws RecognitionException {
        try {
            int _type = T__95;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:72:7: ( '*' )
            // JPA.g:72:9: '*'
            {
            match('*'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__95"

    // $ANTLR start "T__96"
    public final void mT__96() throws RecognitionException {
        try {
            int _type = T__96;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:73:7: ( '/' )
            // JPA.g:73:9: '/'
            {
            match('/'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__96"

    // $ANTLR start "T__97"
    public final void mT__97() throws RecognitionException {
        try {
            int _type = T__97;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:74:7: ( 'LENGTH' )
            // JPA.g:74:9: 'LENGTH'
            {
            match("LENGTH"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__97"

    // $ANTLR start "T__98"
    public final void mT__98() throws RecognitionException {
        try {
            int _type = T__98;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:75:7: ( 'LOCATE' )
            // JPA.g:75:9: 'LOCATE'
            {
            match("LOCATE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__98"

    // $ANTLR start "T__99"
    public final void mT__99() throws RecognitionException {
        try {
            int _type = T__99;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:76:7: ( 'ABS' )
            // JPA.g:76:9: 'ABS'
            {
            match("ABS"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__99"

    // $ANTLR start "T__100"
    public final void mT__100() throws RecognitionException {
        try {
            int _type = T__100;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:77:8: ( 'SQRT' )
            // JPA.g:77:10: 'SQRT'
            {
            match("SQRT"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__100"

    // $ANTLR start "T__101"
    public final void mT__101() throws RecognitionException {
        try {
            int _type = T__101;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:78:8: ( 'MOD' )
            // JPA.g:78:10: 'MOD'
            {
            match("MOD"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__101"

    // $ANTLR start "T__102"
    public final void mT__102() throws RecognitionException {
        try {
            int _type = T__102;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:79:8: ( 'SIZE' )
            // JPA.g:79:10: 'SIZE'
            {
            match("SIZE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__102"

    // $ANTLR start "T__103"
    public final void mT__103() throws RecognitionException {
        try {
            int _type = T__103;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:80:8: ( 'CURRENT_DATE' )
            // JPA.g:80:10: 'CURRENT_DATE'
            {
            match("CURRENT_DATE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__103"

    // $ANTLR start "T__104"
    public final void mT__104() throws RecognitionException {
        try {
            int _type = T__104;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:81:8: ( 'CURRENT_TIME' )
            // JPA.g:81:10: 'CURRENT_TIME'
            {
            match("CURRENT_TIME"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__104"

    // $ANTLR start "T__105"
    public final void mT__105() throws RecognitionException {
        try {
            int _type = T__105;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:82:8: ( 'CURRENT_TIMESTAMP' )
            // JPA.g:82:10: 'CURRENT_TIMESTAMP'
            {
            match("CURRENT_TIMESTAMP"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__105"

    // $ANTLR start "T__106"
    public final void mT__106() throws RecognitionException {
        try {
            int _type = T__106;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:83:8: ( 'CONCAT' )
            // JPA.g:83:10: 'CONCAT'
            {
            match("CONCAT"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__106"

    // $ANTLR start "T__107"
    public final void mT__107() throws RecognitionException {
        try {
            int _type = T__107;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:84:8: ( 'SUBSTRING' )
            // JPA.g:84:10: 'SUBSTRING'
            {
            match("SUBSTRING"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__107"

    // $ANTLR start "T__108"
    public final void mT__108() throws RecognitionException {
        try {
            int _type = T__108;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:85:8: ( 'TRIM' )
            // JPA.g:85:10: 'TRIM'
            {
            match("TRIM"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__108"

    // $ANTLR start "T__109"
    public final void mT__109() throws RecognitionException {
        try {
            int _type = T__109;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:86:8: ( 'LOWER' )
            // JPA.g:86:10: 'LOWER'
            {
            match("LOWER"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__109"

    // $ANTLR start "T__110"
    public final void mT__110() throws RecognitionException {
        try {
            int _type = T__110;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:87:8: ( 'UPPER' )
            // JPA.g:87:10: 'UPPER'
            {
            match("UPPER"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__110"

    // $ANTLR start "T__111"
    public final void mT__111() throws RecognitionException {
        try {
            int _type = T__111;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:88:8: ( 'LEADING' )
            // JPA.g:88:10: 'LEADING'
            {
            match("LEADING"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__111"

    // $ANTLR start "T__112"
    public final void mT__112() throws RecognitionException {
        try {
            int _type = T__112;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:89:8: ( 'TRAILING' )
            // JPA.g:89:10: 'TRAILING'
            {
            match("TRAILING"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__112"

    // $ANTLR start "T__113"
    public final void mT__113() throws RecognitionException {
        try {
            int _type = T__113;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:90:8: ( 'BOTH' )
            // JPA.g:90:10: 'BOTH'
            {
            match("BOTH"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__113"

    // $ANTLR start "T__114"
    public final void mT__114() throws RecognitionException {
        try {
            int _type = T__114;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:91:8: ( '0x' )
            // JPA.g:91:10: '0x'
            {
            match("0x"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__114"

    // $ANTLR start "T__115"
    public final void mT__115() throws RecognitionException {
        try {
            int _type = T__115;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:92:8: ( '?' )
            // JPA.g:92:10: '?'
            {
            match('?'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__115"

    // $ANTLR start "T__116"
    public final void mT__116() throws RecognitionException {
        try {
            int _type = T__116;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:93:8: ( '${' )
            // JPA.g:93:10: '${'
            {
            match("${"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__116"

    // $ANTLR start "T__117"
    public final void mT__117() throws RecognitionException {
        try {
            int _type = T__117;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:94:8: ( '}' )
            // JPA.g:94:10: '}'
            {
            match('}'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__117"

    // $ANTLR start "T__118"
    public final void mT__118() throws RecognitionException {
        try {
            int _type = T__118;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:95:8: ( 'true' )
            // JPA.g:95:10: 'true'
            {
            match("true"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__118"

    // $ANTLR start "T__119"
    public final void mT__119() throws RecognitionException {
        try {
            int _type = T__119;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:96:8: ( 'false' )
            // JPA.g:96:10: 'false'
            {
            match("false"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__119"

    // $ANTLR start "TRIM_CHARACTER"
    public final void mTRIM_CHARACTER() throws RecognitionException {
        try {
            int _type = TRIM_CHARACTER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:450:5: ( '\\'.\\'' )
            // JPA.g:450:7: '\\'.\\''
            {
            match("'.'"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "TRIM_CHARACTER"

    // $ANTLR start "STRINGLITERAL"
    public final void mSTRINGLITERAL() throws RecognitionException {
        try {
            int _type = STRINGLITERAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:453:5: ( '\\'' (~ ( '\\'' | '\"' ) )* '\\'' )
            // JPA.g:453:7: '\\'' (~ ( '\\'' | '\"' ) )* '\\''
            {
            match('\''); 
            // JPA.g:453:12: (~ ( '\\'' | '\"' ) )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0>='\u0000' && LA1_0<='!')||(LA1_0>='#' && LA1_0<='&')||(LA1_0>='(' && LA1_0<='\uFFFF')) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // JPA.g:453:13: ~ ( '\\'' | '\"' )
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='!')||(input.LA(1)>='#' && input.LA(1)<='&')||(input.LA(1)>='(' && input.LA(1)<='\uFFFF') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);

            match('\''); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "STRINGLITERAL"

    // $ANTLR start "WORD"
    public final void mWORD() throws RecognitionException {
        try {
            int _type = WORD;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:456:5: ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '$' )* )
            // JPA.g:456:7: ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '$' )*
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // JPA.g:456:31: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '$' )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0=='$'||(LA2_0>='0' && LA2_0<='9')||(LA2_0>='A' && LA2_0<='Z')||LA2_0=='_'||(LA2_0>='a' && LA2_0<='z')) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // JPA.g:
            	    {
            	    if ( input.LA(1)=='$'||(input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WORD"

    // $ANTLR start "RUSSIAN_SYMBOLS"
    public final void mRUSSIAN_SYMBOLS() throws RecognitionException {
        try {
            int _type = RUSSIAN_SYMBOLS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:459:5: ( ( '\\u0400' .. '\\u04FF' | '\\u0500' .. '\\u052F' ) )
            // JPA.g:459:7: ( '\\u0400' .. '\\u04FF' | '\\u0500' .. '\\u052F' )
            {
            if ( (input.LA(1)>='\u0400' && input.LA(1)<='\u052F') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            if ( 1 == 1) throw new IllegalArgumentException("Incorrect symbol");

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RUSSIAN_SYMBOLS"

    // $ANTLR start "NAMED_PARAMETER"
    public final void mNAMED_PARAMETER() throws RecognitionException {
        try {
            int _type = NAMED_PARAMETER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:462:5: ( ':' ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '$' )* ( ( '.' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '$' )+ )* )
            // JPA.g:462:7: ':' ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '$' )* ( ( '.' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '$' )+ )*
            {
            match(':'); 
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // JPA.g:462:34: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '$' )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0=='$'||(LA3_0>='0' && LA3_0<='9')||(LA3_0>='A' && LA3_0<='Z')||LA3_0=='_'||(LA3_0>='a' && LA3_0<='z')) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // JPA.g:
            	    {
            	    if ( input.LA(1)=='$'||(input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);

            // JPA.g:462:72: ( ( '.' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '$' )+ )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0=='.') ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // JPA.g:462:73: ( '.' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '$' )+
            	    {
            	    // JPA.g:462:73: ( '.' )
            	    // JPA.g:462:74: '.'
            	    {
            	    match('.'); 

            	    }

            	    // JPA.g:462:79: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '$' )+
            	    int cnt4=0;
            	    loop4:
            	    do {
            	        int alt4=2;
            	        int LA4_0 = input.LA(1);

            	        if ( (LA4_0=='$'||(LA4_0>='0' && LA4_0<='9')||(LA4_0>='A' && LA4_0<='Z')||LA4_0=='_'||(LA4_0>='a' && LA4_0<='z')) ) {
            	            alt4=1;
            	        }


            	        switch (alt4) {
            	    	case 1 :
            	    	    // JPA.g:
            	    	    {
            	    	    if ( input.LA(1)=='$'||(input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	    	        input.consume();

            	    	    }
            	    	    else {
            	    	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	    	        recover(mse);
            	    	        throw mse;}


            	    	    }
            	    	    break;

            	    	default :
            	    	    if ( cnt4 >= 1 ) break loop4;
            	                EarlyExitException eee =
            	                    new EarlyExitException(4, input);
            	                throw eee;
            	        }
            	        cnt4++;
            	    } while (true);


            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NAMED_PARAMETER"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:464:5: ( ( ' ' | '\\r' | '\\t' | '\\u000C' | '\\n' ) )
            // JPA.g:464:7: ( ' ' | '\\r' | '\\t' | '\\u000C' | '\\n' )
            {
            if ( (input.LA(1)>='\t' && input.LA(1)<='\n')||(input.LA(1)>='\f' && input.LA(1)<='\r')||input.LA(1)==' ' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            _channel=HIDDEN;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WS"

    // $ANTLR start "COMMENT"
    public final void mCOMMENT() throws RecognitionException {
        try {
            int _type = COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:468:5: ( '/*' ( . )* '*/' )
            // JPA.g:468:7: '/*' ( . )* '*/'
            {
            match("/*"); 

            // JPA.g:468:12: ( . )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0=='*') ) {
                    int LA6_1 = input.LA(2);

                    if ( (LA6_1=='/') ) {
                        alt6=2;
                    }
                    else if ( ((LA6_1>='\u0000' && LA6_1<='.')||(LA6_1>='0' && LA6_1<='\uFFFF')) ) {
                        alt6=1;
                    }


                }
                else if ( ((LA6_0>='\u0000' && LA6_0<=')')||(LA6_0>='+' && LA6_0<='\uFFFF')) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // JPA.g:468:12: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop6;
                }
            } while (true);

            match("*/"); 

            _channel=HIDDEN;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "COMMENT"

    // $ANTLR start "LINE_COMMENT"
    public final void mLINE_COMMENT() throws RecognitionException {
        try {
            int _type = LINE_COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:471:5: ( '//' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n' )
            // JPA.g:471:7: '//' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n'
            {
            match("//"); 

            // JPA.g:471:12: (~ ( '\\n' | '\\r' ) )*
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( ((LA7_0>='\u0000' && LA7_0<='\t')||(LA7_0>='\u000B' && LA7_0<='\f')||(LA7_0>='\u000E' && LA7_0<='\uFFFF')) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // JPA.g:471:12: ~ ( '\\n' | '\\r' )
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='\t')||(input.LA(1)>='\u000B' && input.LA(1)<='\f')||(input.LA(1)>='\u000E' && input.LA(1)<='\uFFFF') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop7;
                }
            } while (true);

            // JPA.g:471:26: ( '\\r' )?
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0=='\r') ) {
                alt8=1;
            }
            switch (alt8) {
                case 1 :
                    // JPA.g:471:26: '\\r'
                    {
                    match('\r'); 

                    }
                    break;

            }

            match('\n'); 
            _channel=HIDDEN;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LINE_COMMENT"

    // $ANTLR start "ESCAPE_CHARACTER"
    public final void mESCAPE_CHARACTER() throws RecognitionException {
        try {
            int _type = ESCAPE_CHARACTER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:474:5: ( '\\'' (~ ( '\\'' | '\\\\' ) ) '\\'' )
            // JPA.g:474:7: '\\'' (~ ( '\\'' | '\\\\' ) ) '\\''
            {
            match('\''); 
            // JPA.g:474:12: (~ ( '\\'' | '\\\\' ) )
            // JPA.g:474:13: ~ ( '\\'' | '\\\\' )
            {
            if ( (input.LA(1)>='\u0000' && input.LA(1)<='&')||(input.LA(1)>='(' && input.LA(1)<='[')||(input.LA(1)>=']' && input.LA(1)<='\uFFFF') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            match('\''); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ESCAPE_CHARACTER"

    // $ANTLR start "INT_NUMERAL"
    public final void mINT_NUMERAL() throws RecognitionException {
        try {
            int _type = INT_NUMERAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // JPA.g:477:5: ( ( '0' .. '9' )+ )
            // JPA.g:477:7: ( '0' .. '9' )+
            {
            // JPA.g:477:7: ( '0' .. '9' )+
            int cnt9=0;
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( ((LA9_0>='0' && LA9_0<='9')) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // JPA.g:477:8: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

            	    }
            	    break;

            	default :
            	    if ( cnt9 >= 1 ) break loop9;
                        EarlyExitException eee =
                            new EarlyExitException(9, input);
                        throw eee;
                }
                cnt9++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT_NUMERAL"

    public void mTokens() throws RecognitionException {
        // JPA.g:1:8: ( HAVING | ASC | DESC | AVG | MAX | MIN | SUM | COUNT | OR | AND | LPAREN | RPAREN | DISTINCT | LEFT | OUTER | INNER | JOIN | FETCH | ORDER | GROUP | BY | T__52 | T__53 | T__54 | T__55 | T__56 | T__57 | T__58 | T__59 | T__60 | T__61 | T__62 | T__63 | T__64 | T__65 | T__66 | T__67 | T__68 | T__69 | T__70 | T__71 | T__72 | T__73 | T__74 | T__75 | T__76 | T__77 | T__78 | T__79 | T__80 | T__81 | T__82 | T__83 | T__84 | T__85 | T__86 | T__87 | T__88 | T__89 | T__90 | T__91 | T__92 | T__93 | T__94 | T__95 | T__96 | T__97 | T__98 | T__99 | T__100 | T__101 | T__102 | T__103 | T__104 | T__105 | T__106 | T__107 | T__108 | T__109 | T__110 | T__111 | T__112 | T__113 | T__114 | T__115 | T__116 | T__117 | T__118 | T__119 | TRIM_CHARACTER | STRINGLITERAL | WORD | RUSSIAN_SYMBOLS | NAMED_PARAMETER | WS | COMMENT | LINE_COMMENT | ESCAPE_CHARACTER | INT_NUMERAL )
        int alt10=99;
        alt10 = dfa10.predict(input);
        switch (alt10) {
            case 1 :
                // JPA.g:1:10: HAVING
                {
                mHAVING(); 

                }
                break;
            case 2 :
                // JPA.g:1:17: ASC
                {
                mASC(); 

                }
                break;
            case 3 :
                // JPA.g:1:21: DESC
                {
                mDESC(); 

                }
                break;
            case 4 :
                // JPA.g:1:26: AVG
                {
                mAVG(); 

                }
                break;
            case 5 :
                // JPA.g:1:30: MAX
                {
                mMAX(); 

                }
                break;
            case 6 :
                // JPA.g:1:34: MIN
                {
                mMIN(); 

                }
                break;
            case 7 :
                // JPA.g:1:38: SUM
                {
                mSUM(); 

                }
                break;
            case 8 :
                // JPA.g:1:42: COUNT
                {
                mCOUNT(); 

                }
                break;
            case 9 :
                // JPA.g:1:48: OR
                {
                mOR(); 

                }
                break;
            case 10 :
                // JPA.g:1:51: AND
                {
                mAND(); 

                }
                break;
            case 11 :
                // JPA.g:1:55: LPAREN
                {
                mLPAREN(); 

                }
                break;
            case 12 :
                // JPA.g:1:62: RPAREN
                {
                mRPAREN(); 

                }
                break;
            case 13 :
                // JPA.g:1:69: DISTINCT
                {
                mDISTINCT(); 

                }
                break;
            case 14 :
                // JPA.g:1:78: LEFT
                {
                mLEFT(); 

                }
                break;
            case 15 :
                // JPA.g:1:83: OUTER
                {
                mOUTER(); 

                }
                break;
            case 16 :
                // JPA.g:1:89: INNER
                {
                mINNER(); 

                }
                break;
            case 17 :
                // JPA.g:1:95: JOIN
                {
                mJOIN(); 

                }
                break;
            case 18 :
                // JPA.g:1:100: FETCH
                {
                mFETCH(); 

                }
                break;
            case 19 :
                // JPA.g:1:106: ORDER
                {
                mORDER(); 

                }
                break;
            case 20 :
                // JPA.g:1:112: GROUP
                {
                mGROUP(); 

                }
                break;
            case 21 :
                // JPA.g:1:118: BY
                {
                mBY(); 

                }
                break;
            case 22 :
                // JPA.g:1:121: T__52
                {
                mT__52(); 

                }
                break;
            case 23 :
                // JPA.g:1:127: T__53
                {
                mT__53(); 

                }
                break;
            case 24 :
                // JPA.g:1:133: T__54
                {
                mT__54(); 

                }
                break;
            case 25 :
                // JPA.g:1:139: T__55
                {
                mT__55(); 

                }
                break;
            case 26 :
                // JPA.g:1:145: T__56
                {
                mT__56(); 

                }
                break;
            case 27 :
                // JPA.g:1:151: T__57
                {
                mT__57(); 

                }
                break;
            case 28 :
                // JPA.g:1:157: T__58
                {
                mT__58(); 

                }
                break;
            case 29 :
                // JPA.g:1:163: T__59
                {
                mT__59(); 

                }
                break;
            case 30 :
                // JPA.g:1:169: T__60
                {
                mT__60(); 

                }
                break;
            case 31 :
                // JPA.g:1:175: T__61
                {
                mT__61(); 

                }
                break;
            case 32 :
                // JPA.g:1:181: T__62
                {
                mT__62(); 

                }
                break;
            case 33 :
                // JPA.g:1:187: T__63
                {
                mT__63(); 

                }
                break;
            case 34 :
                // JPA.g:1:193: T__64
                {
                mT__64(); 

                }
                break;
            case 35 :
                // JPA.g:1:199: T__65
                {
                mT__65(); 

                }
                break;
            case 36 :
                // JPA.g:1:205: T__66
                {
                mT__66(); 

                }
                break;
            case 37 :
                // JPA.g:1:211: T__67
                {
                mT__67(); 

                }
                break;
            case 38 :
                // JPA.g:1:217: T__68
                {
                mT__68(); 

                }
                break;
            case 39 :
                // JPA.g:1:223: T__69
                {
                mT__69(); 

                }
                break;
            case 40 :
                // JPA.g:1:229: T__70
                {
                mT__70(); 

                }
                break;
            case 41 :
                // JPA.g:1:235: T__71
                {
                mT__71(); 

                }
                break;
            case 42 :
                // JPA.g:1:241: T__72
                {
                mT__72(); 

                }
                break;
            case 43 :
                // JPA.g:1:247: T__73
                {
                mT__73(); 

                }
                break;
            case 44 :
                // JPA.g:1:253: T__74
                {
                mT__74(); 

                }
                break;
            case 45 :
                // JPA.g:1:259: T__75
                {
                mT__75(); 

                }
                break;
            case 46 :
                // JPA.g:1:265: T__76
                {
                mT__76(); 

                }
                break;
            case 47 :
                // JPA.g:1:271: T__77
                {
                mT__77(); 

                }
                break;
            case 48 :
                // JPA.g:1:277: T__78
                {
                mT__78(); 

                }
                break;
            case 49 :
                // JPA.g:1:283: T__79
                {
                mT__79(); 

                }
                break;
            case 50 :
                // JPA.g:1:289: T__80
                {
                mT__80(); 

                }
                break;
            case 51 :
                // JPA.g:1:295: T__81
                {
                mT__81(); 

                }
                break;
            case 52 :
                // JPA.g:1:301: T__82
                {
                mT__82(); 

                }
                break;
            case 53 :
                // JPA.g:1:307: T__83
                {
                mT__83(); 

                }
                break;
            case 54 :
                // JPA.g:1:313: T__84
                {
                mT__84(); 

                }
                break;
            case 55 :
                // JPA.g:1:319: T__85
                {
                mT__85(); 

                }
                break;
            case 56 :
                // JPA.g:1:325: T__86
                {
                mT__86(); 

                }
                break;
            case 57 :
                // JPA.g:1:331: T__87
                {
                mT__87(); 

                }
                break;
            case 58 :
                // JPA.g:1:337: T__88
                {
                mT__88(); 

                }
                break;
            case 59 :
                // JPA.g:1:343: T__89
                {
                mT__89(); 

                }
                break;
            case 60 :
                // JPA.g:1:349: T__90
                {
                mT__90(); 

                }
                break;
            case 61 :
                // JPA.g:1:355: T__91
                {
                mT__91(); 

                }
                break;
            case 62 :
                // JPA.g:1:361: T__92
                {
                mT__92(); 

                }
                break;
            case 63 :
                // JPA.g:1:367: T__93
                {
                mT__93(); 

                }
                break;
            case 64 :
                // JPA.g:1:373: T__94
                {
                mT__94(); 

                }
                break;
            case 65 :
                // JPA.g:1:379: T__95
                {
                mT__95(); 

                }
                break;
            case 66 :
                // JPA.g:1:385: T__96
                {
                mT__96(); 

                }
                break;
            case 67 :
                // JPA.g:1:391: T__97
                {
                mT__97(); 

                }
                break;
            case 68 :
                // JPA.g:1:397: T__98
                {
                mT__98(); 

                }
                break;
            case 69 :
                // JPA.g:1:403: T__99
                {
                mT__99(); 

                }
                break;
            case 70 :
                // JPA.g:1:409: T__100
                {
                mT__100(); 

                }
                break;
            case 71 :
                // JPA.g:1:416: T__101
                {
                mT__101(); 

                }
                break;
            case 72 :
                // JPA.g:1:423: T__102
                {
                mT__102(); 

                }
                break;
            case 73 :
                // JPA.g:1:430: T__103
                {
                mT__103(); 

                }
                break;
            case 74 :
                // JPA.g:1:437: T__104
                {
                mT__104(); 

                }
                break;
            case 75 :
                // JPA.g:1:444: T__105
                {
                mT__105(); 

                }
                break;
            case 76 :
                // JPA.g:1:451: T__106
                {
                mT__106(); 

                }
                break;
            case 77 :
                // JPA.g:1:458: T__107
                {
                mT__107(); 

                }
                break;
            case 78 :
                // JPA.g:1:465: T__108
                {
                mT__108(); 

                }
                break;
            case 79 :
                // JPA.g:1:472: T__109
                {
                mT__109(); 

                }
                break;
            case 80 :
                // JPA.g:1:479: T__110
                {
                mT__110(); 

                }
                break;
            case 81 :
                // JPA.g:1:486: T__111
                {
                mT__111(); 

                }
                break;
            case 82 :
                // JPA.g:1:493: T__112
                {
                mT__112(); 

                }
                break;
            case 83 :
                // JPA.g:1:500: T__113
                {
                mT__113(); 

                }
                break;
            case 84 :
                // JPA.g:1:507: T__114
                {
                mT__114(); 

                }
                break;
            case 85 :
                // JPA.g:1:514: T__115
                {
                mT__115(); 

                }
                break;
            case 86 :
                // JPA.g:1:521: T__116
                {
                mT__116(); 

                }
                break;
            case 87 :
                // JPA.g:1:528: T__117
                {
                mT__117(); 

                }
                break;
            case 88 :
                // JPA.g:1:535: T__118
                {
                mT__118(); 

                }
                break;
            case 89 :
                // JPA.g:1:542: T__119
                {
                mT__119(); 

                }
                break;
            case 90 :
                // JPA.g:1:549: TRIM_CHARACTER
                {
                mTRIM_CHARACTER(); 

                }
                break;
            case 91 :
                // JPA.g:1:564: STRINGLITERAL
                {
                mSTRINGLITERAL(); 

                }
                break;
            case 92 :
                // JPA.g:1:578: WORD
                {
                mWORD(); 

                }
                break;
            case 93 :
                // JPA.g:1:583: RUSSIAN_SYMBOLS
                {
                mRUSSIAN_SYMBOLS(); 

                }
                break;
            case 94 :
                // JPA.g:1:599: NAMED_PARAMETER
                {
                mNAMED_PARAMETER(); 

                }
                break;
            case 95 :
                // JPA.g:1:615: WS
                {
                mWS(); 

                }
                break;
            case 96 :
                // JPA.g:1:618: COMMENT
                {
                mCOMMENT(); 

                }
                break;
            case 97 :
                // JPA.g:1:626: LINE_COMMENT
                {
                mLINE_COMMENT(); 

                }
                break;
            case 98 :
                // JPA.g:1:639: ESCAPE_CHARACTER
                {
                mESCAPE_CHARACTER(); 

                }
                break;
            case 99 :
                // JPA.g:1:656: INT_NUMERAL
                {
                mINT_NUMERAL(); 

                }
                break;

        }

    }


    protected DFA10 dfa10 = new DFA10(this);
    static final String DFA10_eotS =
        "\1\uffff\7\47\1\106\1\uffff\6\47\2\uffff\2\47\3\uffff\2\47\1\uffff"+
        "\1\140\1\142\1\uffff\1\145\2\47\1\53\3\uffff\2\47\6\uffff\2\47\1"+
        "\162\22\47\1\u008b\2\47\1\u008e\2\uffff\3\47\1\u0096\1\u0097\4\47"+
        "\1\u009c\6\47\3\uffff\4\47\10\uffff\2\47\1\uffff\2\47\4\uffff\2"+
        "\47\1\u00b2\1\uffff\1\u00b3\1\u00b4\1\u00b5\1\u00b6\1\u00b7\2\47"+
        "\1\u00ba\1\u00bb\1\u00bd\1\47\1\u00bf\1\47\1\u00c1\12\47\1\uffff"+
        "\2\47\1\uffff\7\47\2\uffff\4\47\1\uffff\2\47\1\u00db\1\u00dc\1\u00dd"+
        "\2\47\1\uffff\11\47\2\uffff\1\47\1\u00ec\6\uffff\1\u00ed\1\47\2"+
        "\uffff\1\47\1\uffff\1\47\1\uffff\1\47\1\uffff\3\47\1\u00f5\1\u00f6"+
        "\1\u00f7\6\47\1\u00fe\2\47\1\u0101\3\47\1\u0105\1\47\1\u0107\2\47"+
        "\1\u010a\3\uffff\1\u010b\1\47\1\uffff\1\u010e\3\47\1\u0112\2\47"+
        "\1\u0115\1\47\1\uffff\1\47\2\uffff\2\47\1\u011a\4\47\3\uffff\1\u011f"+
        "\2\47\1\u0122\1\u0123\1\47\1\uffff\2\47\1\uffff\1\47\1\u0128\1\u0129"+
        "\1\uffff\1\u012a\1\uffff\1\u012b\1\47\2\uffff\1\u012d\2\uffff\1"+
        "\47\1\u0132\1\47\1\uffff\1\47\1\u0135\1\uffff\1\u0136\1\u0137\1"+
        "\47\1\u0139\1\uffff\1\u013a\1\47\1\u013c\1\u013d\1\uffff\1\u013e"+
        "\1\47\2\uffff\1\u0140\1\u0141\1\47\1\u0143\4\uffff\1\47\4\uffff"+
        "\1\u0145\1\uffff\1\u0146\1\47\3\uffff\1\47\2\uffff\1\47\3\uffff"+
        "\1\47\2\uffff\1\u014b\1\uffff\1\u014c\2\uffff\1\47\1\u014e\2\47"+
        "\2\uffff\1\u0152\1\uffff\1\u0153\2\47\2\uffff\4\47\1\u015a\1\u015c"+
        "\1\uffff\1\47\1\uffff\3\47\1\u0161\1\uffff";
    static final String DFA10_eofS =
        "\u0162\uffff";
    static final String DFA10_minS =
        "\1\11\1\101\1\102\2\101\1\105\1\117\1\102\1\123\1\uffff\1\105\1"+
        "\116\1\117\1\105\1\122\1\105\2\uffff\1\105\1\110\1\102\2\uffff\1"+
        "\105\1\115\1\uffff\2\75\1\uffff\1\52\1\122\1\120\1\170\3\uffff\1"+
        "\162\1\141\1\0\5\uffff\1\126\1\125\1\44\1\107\1\104\1\114\3\123"+
        "\1\131\1\130\1\116\1\104\1\115\1\102\1\103\1\115\1\122\1\132\1\116"+
        "\1\122\1\44\1\124\1\112\1\44\2\uffff\1\101\1\113\1\103\2\44\1\111"+
        "\1\124\2\117\1\44\2\124\1\127\1\124\1\114\1\105\1\uffff\1\101\1"+
        "\uffff\1\101\1\103\1\120\1\111\10\uffff\1\101\1\120\1\uffff\1\165"+
        "\1\154\2\0\2\uffff\1\111\1\122\1\44\1\uffff\5\44\1\103\1\124\3\44"+
        "\1\124\1\44\1\102\1\44\1\123\1\105\1\117\1\105\1\124\1\105\1\116"+
        "\1\103\1\122\1\105\1\uffff\2\105\1\uffff\1\124\1\107\1\104\1\105"+
        "\1\101\2\105\2\uffff\1\116\1\103\1\115\1\125\1\uffff\1\127\1\110"+
        "\3\44\1\114\1\122\1\124\1\122\1\101\1\124\1\123\1\115\1\111\1\105"+
        "\1\145\1\163\2\uffff\1\116\1\44\6\uffff\1\44\1\111\2\uffff\1\124"+
        "\1\uffff\1\110\1\uffff\1\105\1\uffff\1\124\1\103\1\116\3\44\1\124"+
        "\1\101\1\105\2\122\1\103\1\44\1\124\1\111\1\44\1\124\2\122\1\44"+
        "\1\110\1\44\1\120\1\105\1\44\3\uffff\1\44\2\105\1\44\1\120\1\131"+
        "\1\124\1\44\1\114\1\122\1\44\1\145\1\uffff\1\107\2\uffff\1\116\1"+
        "\105\1\44\2\122\1\124\1\104\3\uffff\1\44\1\124\1\116\2\44\1\124"+
        "\1\uffff\1\110\1\116\1\uffff\1\105\2\44\1\uffff\1\44\1\uffff\1\44"+
        "\1\105\2\uffff\1\44\1\101\1\uffff\1\105\1\44\1\123\1\uffff\1\111"+
        "\1\44\1\uffff\2\44\1\103\1\44\1\uffff\1\44\1\111\2\44\1\uffff\1"+
        "\44\1\124\2\uffff\2\44\1\107\1\44\4\uffff\1\116\4\uffff\1\44\1\uffff"+
        "\1\44\1\116\3\uffff\1\124\2\uffff\1\116\3\uffff\1\137\2\uffff\1"+
        "\44\1\uffff\1\44\2\uffff\1\107\1\44\1\107\1\104\2\uffff\1\44\1\uffff"+
        "\1\44\1\101\1\111\2\uffff\1\124\1\115\2\105\2\44\1\uffff\1\124\1"+
        "\uffff\1\101\1\115\1\120\1\44\1\uffff";
    static final String DFA10_maxS =
        "\1\u052f\1\117\1\126\1\111\1\117\3\125\1\123\1\uffff\1\117\1\123"+
        "\1\117\2\122\1\131\2\uffff\1\125\1\110\1\124\2\uffff\1\105\1\130"+
        "\1\uffff\1\76\1\75\1\uffff\1\57\1\122\1\120\1\170\3\uffff\1\162"+
        "\1\141\1\uffff\5\uffff\1\126\1\125\1\172\1\107\1\131\1\114\3\123"+
        "\1\131\1\130\2\116\2\115\1\114\1\115\1\122\1\132\1\125\1\122\1\172"+
        "\1\124\1\112\1\172\2\uffff\1\116\1\113\1\127\2\172\1\111\1\124\2"+
        "\117\1\172\2\124\2\127\1\114\1\105\1\uffff\1\101\1\uffff\1\101\1"+
        "\103\1\120\1\111\10\uffff\1\111\1\120\1\uffff\1\165\1\154\2\uffff"+
        "\2\uffff\1\111\1\122\1\172\1\uffff\5\172\1\103\1\124\3\172\1\124"+
        "\1\172\1\102\1\172\1\123\1\105\1\117\1\105\1\124\1\105\1\116\1\103"+
        "\1\122\1\105\1\uffff\2\105\1\uffff\1\124\1\107\1\104\1\105\1\101"+
        "\2\105\2\uffff\1\116\1\103\1\115\1\125\1\uffff\1\127\1\110\3\172"+
        "\1\114\1\122\1\124\1\122\1\101\1\124\1\123\1\115\1\111\1\105\1\145"+
        "\1\163\2\uffff\1\116\1\172\6\uffff\1\172\1\111\2\uffff\1\124\1\uffff"+
        "\1\110\1\uffff\1\105\1\uffff\1\124\1\103\1\116\3\172\1\124\1\101"+
        "\1\105\2\122\1\103\1\172\1\124\1\111\1\172\1\124\2\122\1\172\1\110"+
        "\1\172\1\120\1\105\1\172\3\uffff\1\172\2\105\1\172\1\120\1\131\1"+
        "\124\1\172\1\114\1\122\1\172\1\145\1\uffff\1\107\2\uffff\1\116\1"+
        "\105\1\172\2\122\1\124\1\104\3\uffff\1\172\1\124\1\116\2\172\1\124"+
        "\1\uffff\1\110\1\116\1\uffff\1\105\2\172\1\uffff\1\172\1\uffff\1"+
        "\172\1\105\2\uffff\1\172\1\105\1\uffff\1\105\1\172\1\123\1\uffff"+
        "\1\111\1\172\1\uffff\2\172\1\103\1\172\1\uffff\1\172\1\111\2\172"+
        "\1\uffff\1\172\1\124\2\uffff\2\172\1\107\1\172\4\uffff\1\116\4\uffff"+
        "\1\172\1\uffff\1\172\1\116\3\uffff\1\124\2\uffff\1\116\3\uffff\1"+
        "\137\2\uffff\1\172\1\uffff\1\172\2\uffff\1\107\1\172\1\107\1\124"+
        "\2\uffff\1\172\1\uffff\1\172\1\101\1\111\2\uffff\1\124\1\115\2\105"+
        "\2\172\1\uffff\1\124\1\uffff\1\101\1\115\1\120\1\172\1\uffff";
    static final String DFA10_acceptS =
        "\11\uffff\1\14\6\uffff\1\30\1\33\3\uffff\1\43\1\44\2\uffff\1\73"+
        "\2\uffff\1\101\4\uffff\1\125\1\126\1\127\3\uffff\1\134\1\135\1\136"+
        "\1\137\1\143\31\uffff\1\32\1\13\20\uffff\1\41\1\uffff\1\56\4\uffff"+
        "\1\74\1\100\1\77\1\76\1\75\1\140\1\141\1\102\2\uffff\1\124\4\uffff"+
        "\1\142\1\133\3\uffff\1\31\30\uffff\1\11\2\uffff\1\66\7\uffff\1\34"+
        "\1\62\4\uffff\1\25\21\uffff\1\132\1\133\2\uffff\1\2\1\4\1\12\1\71"+
        "\1\70\1\105\2\uffff\1\47\1\5\1\uffff\1\6\1\uffff\1\107\1\uffff\1"+
        "\7\31\uffff\1\36\1\40\1\42\14\uffff\1\132\1\uffff\1\50\1\3\7\uffff"+
        "\1\72\1\106\1\110\6\uffff\1\16\2\uffff\1\60\3\uffff\1\21\1\uffff"+
        "\1\27\2\uffff\1\123\1\63\2\uffff\1\45\3\uffff\1\116\2\uffff\1\130"+
        "\4\uffff\1\46\4\uffff\1\10\2\uffff\1\23\1\17\4\uffff\1\117\1\20"+
        "\1\22\1\24\1\uffff\1\37\1\53\1\54\1\55\1\uffff\1\64\2\uffff\1\120"+
        "\1\131\1\1\1\uffff\1\51\1\65\1\uffff\1\26\1\52\1\114\1\uffff\1\35"+
        "\1\103\1\uffff\1\104\1\uffff\1\61\1\67\4\uffff\1\121\1\57\1\uffff"+
        "\1\15\3\uffff\1\122\1\115\6\uffff\1\111\1\uffff\1\112\4\uffff\1"+
        "\113";
    static final String DFA10_specialS =
        "\46\uffff\1\1\104\uffff\1\2\1\0\u00f5\uffff}>";
    static final String[] DFA10_transitionS = {
            "\2\52\1\uffff\2\52\22\uffff\1\52\3\uffff\1\42\2\uffff\1\46"+
            "\1\10\1\11\1\34\1\25\1\20\1\26\1\21\1\35\1\40\11\53\1\51\1\uffff"+
            "\1\32\1\31\1\33\1\41\1\24\1\2\1\17\1\6\1\3\1\30\1\15\1\16\1"+
            "\1\1\13\1\14\1\47\1\12\1\4\1\22\1\7\3\47\1\5\1\36\1\37\1\47"+
            "\1\23\1\47\1\27\1\47\4\uffff\1\47\1\uffff\5\47\1\45\15\47\1"+
            "\44\6\47\2\uffff\1\43\u0382\uffff\u0130\50",
            "\1\54\15\uffff\1\55",
            "\1\62\11\uffff\1\61\1\uffff\1\60\4\uffff\1\56\2\uffff\1\57",
            "\1\65\3\uffff\1\63\3\uffff\1\64",
            "\1\66\3\uffff\1\71\3\uffff\1\67\5\uffff\1\70",
            "\1\73\3\uffff\1\76\5\uffff\1\74\1\uffff\1\75\3\uffff\1\72",
            "\1\77\5\uffff\1\100",
            "\1\103\3\uffff\1\104\13\uffff\1\101\2\uffff\1\102",
            "\1\105",
            "",
            "\1\107\3\uffff\1\110\5\uffff\1\111",
            "\1\112\4\uffff\1\113",
            "\1\114",
            "\1\115\14\uffff\1\116",
            "\1\117",
            "\1\121\11\uffff\1\122\11\uffff\1\120",
            "",
            "",
            "\1\123\11\uffff\1\124\5\uffff\1\125",
            "\1\126",
            "\1\127\1\uffff\1\130\17\uffff\1\131",
            "",
            "",
            "\1\132",
            "\1\134\5\uffff\1\133\4\uffff\1\135",
            "",
            "\1\137\1\136",
            "\1\141",
            "",
            "\1\143\4\uffff\1\144",
            "\1\146",
            "\1\147",
            "\1\150",
            "",
            "",
            "",
            "\1\151",
            "\1\152",
            "\42\154\1\155\4\154\1\156\6\154\1\153\55\154\1\156\uffa3\154",
            "",
            "",
            "",
            "",
            "",
            "\1\157",
            "\1\160",
            "\1\47\13\uffff\12\47\7\uffff\2\47\1\161\27\47\4\uffff\1\47"+
            "\1\uffff\32\47",
            "\1\163",
            "\1\164\24\uffff\1\165",
            "\1\166",
            "\1\167",
            "\1\170",
            "\1\171",
            "\1\172",
            "\1\173",
            "\1\174",
            "\1\176\11\uffff\1\175",
            "\1\177",
            "\1\u0081\12\uffff\1\u0080",
            "\1\u0083\10\uffff\1\u0082",
            "\1\u0084",
            "\1\u0085",
            "\1\u0086",
            "\1\u0088\6\uffff\1\u0087",
            "\1\u0089",
            "\1\47\13\uffff\12\47\7\uffff\3\47\1\u008a\26\47\4\uffff\1"+
            "\47\1\uffff\32\47",
            "\1\u008c",
            "\1\u008d",
            "\1\47\13\uffff\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32"+
            "\47",
            "",
            "",
            "\1\u0091\4\uffff\1\u008f\7\uffff\1\u0090",
            "\1\u0092",
            "\1\u0093\23\uffff\1\u0094",
            "\1\47\13\uffff\12\47\7\uffff\15\47\1\u0095\14\47\4\uffff\1"+
            "\47\1\uffff\32\47",
            "\1\47\13\uffff\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32"+
            "\47",
            "\1\u0098",
            "\1\u0099",
            "\1\u009a",
            "\1\u009b",
            "\1\47\13\uffff\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32"+
            "\47",
            "\1\u009d",
            "\1\u009e",
            "\1\u009f",
            "\1\u00a0\2\uffff\1\u00a1",
            "\1\u00a2",
            "\1\u00a3",
            "",
            "\1\u00a4",
            "",
            "\1\u00a5",
            "\1\u00a6",
            "\1\u00a7",
            "\1\u00a8",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\u00aa\7\uffff\1\u00a9",
            "\1\u00ab",
            "",
            "\1\u00ac",
            "\1\u00ad",
            "\42\156\1\uffff\4\156\1\u00ae\uffd8\156",
            "\42\156\1\uffff\4\156\1\u00af\uffd8\156",
            "",
            "",
            "\1\u00b0",
            "\1\u00b1",
            "\1\47\13\uffff\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32"+
            "\47",
            "",
            "\1\47\13\uffff\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32"+
            "\47",
            "\1\47\13\uffff\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32"+
            "\47",
            "\1\47\13\uffff\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32"+
            "\47",
            "\1\47\13\uffff\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32"+
            "\47",
            "\1\47\13\uffff\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32"+
            "\47",
            "\1\u00b8",
            "\1\u00b9",
            "\1\47\13\uffff\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32"+
            "\47",
            "\1\47\13\uffff\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32"+
            "\47",
            "\1\47\13\uffff\12\47\7\uffff\24\47\1\u00bc\5\47\4\uffff\1"+
            "\47\1\uffff\32\47",
            "\1\u00be",
            "\1\47\13\uffff\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32"+
            "\47",
            "\1\u00c0",
            "\1\47\13\uffff\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32"+
            "\47",
            "\1\u00c2",
            "\1\u00c3",
            "\1\u00c4",
            "\1\u00c5",
            "\1\u00c6",
            "\1\u00c7",
            "\1\u00c8",
            "\1\u00c9",
            "\1\u00ca",
            "\1\u00cb",
            "",
            "\1\u00cc",
            "\1\u00cd",
            "",
            "\1\u00ce",
            "\1\u00cf",
            "\1\u00d0",
            "\1\u00d1",
            "\1\u00d2",
            "\1\u00d3",
            "\1\u00d4",
            "",
            "",
            "\1\u00d5",
            "\1\u00d6",
            "\1\u00d7",
            "\1\u00d8",
            "",
            "\1\u00d9",
            "\1\u00da",
            "\1\47\13\uffff\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32"+
            "\47",
            "\1\47\13\uffff\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32"+
            "\47",
            "\1\47\13\uffff\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32"+
            "\47",
            "\1\u00de",
            "\1\u00df",
            "\1\u00e0",
            "\1\u00e1",
            "\1\u00e2",
            "\1\u00e3",
            "\1\u00e4",
            "\1\u00e5",
            "\1\u00e6",
            "\1\u00e7",
            "\1\u00e8",
            "\1\u00e9",
            "",
            "",
            "\1\u00eb",
            "\1\47\13\uffff\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32"+
            "\47",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\47\13\uffff\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32"+
            "\47",
            "\1\u00ee",
            "",
            "",
            "\1\u00ef",
            "",
            "\1\u00f0",
            "",
            "\1\u00f1",
            "",
            "\1\u00f2",
            "\1\u00f3",
            "\1\u00f4",
            "\1\47\13\uffff\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32"+
            "\47",
            "\1\47\13\uffff\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32"+
            "\47",
            "\1\47\13\uffff\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32"+
            "\47",
            "\1\u00f8",
            "\1\u00f9",
            "\1\u00fa",
            "\1\u00fb",
            "\1\u00fc",
            "\1\u00fd",
            "\1\47\13\uffff\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32"+
            "\47",
            "\1\u00ff",
            "\1\u0100",
            "\1\47\13\uffff\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32"+
            "\47",
            "\1\u0102",
            "\1\u0103",
            "\1\u0104",
            "\1\47\13\uffff\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32"+
            "\47",
            "\1\u0106",
            "\1\47\13\uffff\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32"+
            "\47",
            "\1\u0108",
            "\1\u0109",
            "\1\47\13\uffff\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32"+
            "\47",
            "",
            "",
            "",
            "\1\47\13\uffff\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32"+
            "\47",
            "\1\u010c",
            "\1\u010d",
            "\1\47\13\uffff\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32"+
            "\47",
            "\1\u010f",
            "\1\u0110",
            "\1\u0111",
            "\1\47\13\uffff\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32"+
            "\47",
            "\1\u0113",
            "\1\u0114",
            "\1\47\13\uffff\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32"+
            "\47",
            "\1\u0116",
            "",
            "\1\u0117",
            "",
            "",
            "\1\u0118",
            "\1\u0119",
            "\1\47\13\uffff\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32"+
            "\47",
            "\1\u011b",
            "\1\u011c",
            "\1\u011d",
            "\1\u011e",
            "",
            "",
            "",
            "\1\47\13\uffff\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32"+
            "\47",
            "\1\u0120",
            "\1\u0121",
            "\1\47\13\uffff\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32"+
            "\47",
            "\1\47\13\uffff\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32"+
            "\47",
            "\1\u0124",
            "",
            "\1\u0125",
            "\1\u0126",
            "",
            "\1\u0127",
            "\1\47\13\uffff\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32"+
            "\47",
            "\1\47\13\uffff\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32"+
            "\47",
            "",
            "\1\47\13\uffff\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32"+
            "\47",
            "",
            "\1\47\13\uffff\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32"+
            "\47",
            "\1\u012c",
            "",
            "",
            "\1\47\13\uffff\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32"+
            "\47",
            "\1\u012f\1\u012e\2\uffff\1\u0130",
            "",
            "\1\u0131",
            "\1\47\13\uffff\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32"+
            "\47",
            "\1\u0133",
            "",
            "\1\u0134",
            "\1\47\13\uffff\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32"+
            "\47",
            "",
            "\1\47\13\uffff\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32"+
            "\47",
            "\1\47\13\uffff\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32"+
            "\47",
            "\1\u0138",
            "\1\47\13\uffff\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32"+
            "\47",
            "",
            "\1\47\13\uffff\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32"+
            "\47",
            "\1\u013b",
            "\1\47\13\uffff\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32"+
            "\47",
            "\1\47\13\uffff\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32"+
            "\47",
            "",
            "\1\47\13\uffff\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32"+
            "\47",
            "\1\u013f",
            "",
            "",
            "\1\47\13\uffff\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32"+
            "\47",
            "\1\47\13\uffff\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32"+
            "\47",
            "\1\u0142",
            "\1\47\13\uffff\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32"+
            "\47",
            "",
            "",
            "",
            "",
            "\1\u0144",
            "",
            "",
            "",
            "",
            "\1\47\13\uffff\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32"+
            "\47",
            "",
            "\1\47\13\uffff\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32"+
            "\47",
            "\1\u0147",
            "",
            "",
            "",
            "\1\u0148",
            "",
            "",
            "\1\u0149",
            "",
            "",
            "",
            "\1\u014a",
            "",
            "",
            "\1\47\13\uffff\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32"+
            "\47",
            "",
            "\1\47\13\uffff\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32"+
            "\47",
            "",
            "",
            "\1\u014d",
            "\1\47\13\uffff\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32"+
            "\47",
            "\1\u014f",
            "\1\u0150\17\uffff\1\u0151",
            "",
            "",
            "\1\47\13\uffff\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32"+
            "\47",
            "",
            "\1\47\13\uffff\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32"+
            "\47",
            "\1\u0154",
            "\1\u0155",
            "",
            "",
            "\1\u0156",
            "\1\u0157",
            "\1\u0158",
            "\1\u0159",
            "\1\47\13\uffff\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32"+
            "\47",
            "\1\47\13\uffff\12\47\7\uffff\22\47\1\u015b\7\47\4\uffff\1"+
            "\47\1\uffff\32\47",
            "",
            "\1\u015d",
            "",
            "\1\u015e",
            "\1\u015f",
            "\1\u0160",
            "\1\47\13\uffff\12\47\7\uffff\32\47\4\uffff\1\47\1\uffff\32"+
            "\47",
            ""
    };

    static final short[] DFA10_eot = DFA.unpackEncodedString(DFA10_eotS);
    static final short[] DFA10_eof = DFA.unpackEncodedString(DFA10_eofS);
    static final char[] DFA10_min = DFA.unpackEncodedStringToUnsignedChars(DFA10_minS);
    static final char[] DFA10_max = DFA.unpackEncodedStringToUnsignedChars(DFA10_maxS);
    static final short[] DFA10_accept = DFA.unpackEncodedString(DFA10_acceptS);
    static final short[] DFA10_special = DFA.unpackEncodedString(DFA10_specialS);
    static final short[][] DFA10_transition;

    static {
        int numStates = DFA10_transitionS.length;
        DFA10_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA10_transition[i] = DFA.unpackEncodedString(DFA10_transitionS[i]);
        }
    }

    class DFA10 extends DFA {

        public DFA10(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 10;
            this.eot = DFA10_eot;
            this.eof = DFA10_eof;
            this.min = DFA10_min;
            this.max = DFA10_max;
            this.accept = DFA10_accept;
            this.special = DFA10_special;
            this.transition = DFA10_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( HAVING | ASC | DESC | AVG | MAX | MIN | SUM | COUNT | OR | AND | LPAREN | RPAREN | DISTINCT | LEFT | OUTER | INNER | JOIN | FETCH | ORDER | GROUP | BY | T__52 | T__53 | T__54 | T__55 | T__56 | T__57 | T__58 | T__59 | T__60 | T__61 | T__62 | T__63 | T__64 | T__65 | T__66 | T__67 | T__68 | T__69 | T__70 | T__71 | T__72 | T__73 | T__74 | T__75 | T__76 | T__77 | T__78 | T__79 | T__80 | T__81 | T__82 | T__83 | T__84 | T__85 | T__86 | T__87 | T__88 | T__89 | T__90 | T__91 | T__92 | T__93 | T__94 | T__95 | T__96 | T__97 | T__98 | T__99 | T__100 | T__101 | T__102 | T__103 | T__104 | T__105 | T__106 | T__107 | T__108 | T__109 | T__110 | T__111 | T__112 | T__113 | T__114 | T__115 | T__116 | T__117 | T__118 | T__119 | TRIM_CHARACTER | STRINGLITERAL | WORD | RUSSIAN_SYMBOLS | NAMED_PARAMETER | WS | COMMENT | LINE_COMMENT | ESCAPE_CHARACTER | INT_NUMERAL );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA10_108 = input.LA(1);

                        s = -1;
                        if ( (LA10_108=='\'') ) {s = 175;}

                        else if ( ((LA10_108>='\u0000' && LA10_108<='!')||(LA10_108>='#' && LA10_108<='&')||(LA10_108>='(' && LA10_108<='\uFFFF')) ) {s = 110;}

                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA10_38 = input.LA(1);

                        s = -1;
                        if ( (LA10_38=='.') ) {s = 107;}

                        else if ( ((LA10_38>='\u0000' && LA10_38<='!')||(LA10_38>='#' && LA10_38<='&')||(LA10_38>='(' && LA10_38<='-')||(LA10_38>='/' && LA10_38<='[')||(LA10_38>=']' && LA10_38<='\uFFFF')) ) {s = 108;}

                        else if ( (LA10_38=='\"') ) {s = 109;}

                        else if ( (LA10_38=='\''||LA10_38=='\\') ) {s = 110;}

                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA10_107 = input.LA(1);

                        s = -1;
                        if ( (LA10_107=='\'') ) {s = 174;}

                        else if ( ((LA10_107>='\u0000' && LA10_107<='!')||(LA10_107>='#' && LA10_107<='&')||(LA10_107>='(' && LA10_107<='\uFFFF')) ) {s = 110;}

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 10, _s, input);
            error(nvae);
            throw nvae;
        }
    }

    @Override
    public void emitErrorMessage(String msg) {
        //do nothing
    }
}