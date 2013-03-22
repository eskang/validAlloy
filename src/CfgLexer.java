// $ANTLR 3.4 /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g 2013-03-14 19:18:50

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class CfgLexer extends Lexer {
    public static final int EOF=-1;
    public static final int T__13=13;
    public static final int T__14=14;
    public static final int T__15=15;
    public static final int T__16=16;
    public static final int T__17=17;
    public static final int T__18=18;
    public static final int T__19=19;
    public static final int T__20=20;
    public static final int T__21=21;
    public static final int T__22=22;
    public static final int T__23=23;
    public static final int T__24=24;
    public static final int T__25=25;
    public static final int CHAR=4;
    public static final int ESC_SEQ=5;
    public static final int HEX_DIGIT=6;
    public static final int ID=7;
    public static final int INT=8;
    public static final int OCTAL_ESC=9;
    public static final int STRING=10;
    public static final int UNICODE_ESC=11;
    public static final int WS=12;

    // delegates
    // delegators
    public Lexer[] getDelegates() {
        return new Lexer[] {};
    }

    public CfgLexer() {} 
    public CfgLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public CfgLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);
    }
    public String getGrammarFileName() { return "/home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g"; }

    // $ANTLR start "T__13"
    public final void mT__13() throws RecognitionException {
        try {
            int _type = T__13;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:2:7: ( '#' )
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:2:9: '#'
            {
            match('#'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__13"

    // $ANTLR start "T__14"
    public final void mT__14() throws RecognitionException {
        try {
            int _type = T__14;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:3:7: ( ',' )
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:3:9: ','
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
    // $ANTLR end "T__14"

    // $ANTLR start "T__15"
    public final void mT__15() throws RecognitionException {
        try {
            int _type = T__15;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:4:7: ( ':' )
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:4:9: ':'
            {
            match(':'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__15"

    // $ANTLR start "T__16"
    public final void mT__16() throws RecognitionException {
        try {
            int _type = T__16;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:5:7: ( ';' )
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:5:9: ';'
            {
            match(';'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__16"

    // $ANTLR start "T__17"
    public final void mT__17() throws RecognitionException {
        try {
            int _type = T__17;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:6:7: ( '[' )
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:6:9: '['
            {
            match('['); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__17"

    // $ANTLR start "T__18"
    public final void mT__18() throws RecognitionException {
        try {
            int _type = T__18;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:7:7: ( ']' )
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:7:9: ']'
            {
            match(']'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__18"

    // $ANTLR start "T__19"
    public final void mT__19() throws RecognitionException {
        try {
            int _type = T__19;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:8:7: ( 'but exactly' )
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:8:9: 'but exactly'
            {
            match("but exactly"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__19"

    // $ANTLR start "T__20"
    public final void mT__20() throws RecognitionException {
        try {
            int _type = T__20;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:9:7: ( 'cmd' )
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:9:9: 'cmd'
            {
            match("cmd"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__20"

    // $ANTLR start "T__21"
    public final void mT__21() throws RecognitionException {
        try {
            int _type = T__21;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:10:7: ( 'for' )
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:10:9: 'for'
            {
            match("for"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__21"

    // $ANTLR start "T__22"
    public final void mT__22() throws RecognitionException {
        try {
            int _type = T__22;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:11:7: ( 'git' )
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:11:9: 'git'
            {
            match("git"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__22"

    // $ANTLR start "T__23"
    public final void mT__23() throws RecognitionException {
        try {
            int _type = T__23;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:12:7: ( 'pred' )
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:12:9: 'pred'
            {
            match("pred"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__23"

    // $ANTLR start "T__24"
    public final void mT__24() throws RecognitionException {
        try {
            int _type = T__24;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:13:7: ( 'runs' )
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:13:9: 'runs'
            {
            match("runs"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__24"

    // $ANTLR start "T__25"
    public final void mT__25() throws RecognitionException {
        try {
            int _type = T__25;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:14:7: ( 'scope' )
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:14:9: 'scope'
            {
            match("scope"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__25"

    // $ANTLR start "ID"
    public final void mID() throws RecognitionException {
        try {
            int _type = ID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:208:5: ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '-' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '-' )* )
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:208:7: ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '-' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '-' )*
            {
            if ( input.LA(1)=='-'||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:208:35: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '-' )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0=='-'||(LA1_0 >= '0' && LA1_0 <= '9')||(LA1_0 >= 'A' && LA1_0 <= 'Z')||LA1_0=='_'||(LA1_0 >= 'a' && LA1_0 <= 'z')) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:
            	    {
            	    if ( input.LA(1)=='-'||(input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "ID"

    // $ANTLR start "INT"
    public final void mINT() throws RecognitionException {
        try {
            int _type = INT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:211:5: ( ( '0' .. '9' )+ )
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:211:7: ( '0' .. '9' )+
            {
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:211:7: ( '0' .. '9' )+
            int cnt2=0;
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0 >= '0' && LA2_0 <= '9')) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:
            	    {
            	    if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt2 >= 1 ) break loop2;
                        EarlyExitException eee =
                            new EarlyExitException(2, input);
                        throw eee;
                }
                cnt2++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "INT"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:214:5: ( ( ' ' | '\\t' | '\\r' | '\\n' ) )
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:214:9: ( ' ' | '\\t' | '\\r' | '\\n' )
            {
            if ( (input.LA(1) >= '\t' && input.LA(1) <= '\n')||input.LA(1)=='\r'||input.LA(1)==' ' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            _channel=HIDDEN;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "WS"

    // $ANTLR start "STRING"
    public final void mSTRING() throws RecognitionException {
        try {
            int _type = STRING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:222:5: ( '\"' ( ESC_SEQ |~ ( '\\\\' | '\"' ) )* '\"' )
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:222:8: '\"' ( ESC_SEQ |~ ( '\\\\' | '\"' ) )* '\"'
            {
            match('\"'); 

            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:222:12: ( ESC_SEQ |~ ( '\\\\' | '\"' ) )*
            loop3:
            do {
                int alt3=3;
                int LA3_0 = input.LA(1);

                if ( (LA3_0=='\\') ) {
                    alt3=1;
                }
                else if ( ((LA3_0 >= '\u0000' && LA3_0 <= '!')||(LA3_0 >= '#' && LA3_0 <= '[')||(LA3_0 >= ']' && LA3_0 <= '\uFFFF')) ) {
                    alt3=2;
                }


                switch (alt3) {
            	case 1 :
            	    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:222:14: ESC_SEQ
            	    {
            	    mESC_SEQ(); 


            	    }
            	    break;
            	case 2 :
            	    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:222:24: ~ ( '\\\\' | '\"' )
            	    {
            	    if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '!')||(input.LA(1) >= '#' && input.LA(1) <= '[')||(input.LA(1) >= ']' && input.LA(1) <= '\uFFFF') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);


            match('\"'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "STRING"

    // $ANTLR start "CHAR"
    public final void mCHAR() throws RecognitionException {
        try {
            int _type = CHAR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:225:5: ( '\\'' ( ESC_SEQ |~ ( '\\'' | '\\\\' ) ) '\\'' )
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:225:8: '\\'' ( ESC_SEQ |~ ( '\\'' | '\\\\' ) ) '\\''
            {
            match('\''); 

            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:225:13: ( ESC_SEQ |~ ( '\\'' | '\\\\' ) )
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0=='\\') ) {
                alt4=1;
            }
            else if ( ((LA4_0 >= '\u0000' && LA4_0 <= '&')||(LA4_0 >= '(' && LA4_0 <= '[')||(LA4_0 >= ']' && LA4_0 <= '\uFFFF')) ) {
                alt4=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                throw nvae;

            }
            switch (alt4) {
                case 1 :
                    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:225:15: ESC_SEQ
                    {
                    mESC_SEQ(); 


                    }
                    break;
                case 2 :
                    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:225:25: ~ ( '\\'' | '\\\\' )
                    {
                    if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '&')||(input.LA(1) >= '(' && input.LA(1) <= '[')||(input.LA(1) >= ']' && input.LA(1) <= '\uFFFF') ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    }
                    break;

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
    // $ANTLR end "CHAR"

    // $ANTLR start "HEX_DIGIT"
    public final void mHEX_DIGIT() throws RecognitionException {
        try {
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:230:11: ( ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) )
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:
            {
            if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'F')||(input.LA(1) >= 'a' && input.LA(1) <= 'f') ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "HEX_DIGIT"

    // $ANTLR start "ESC_SEQ"
    public final void mESC_SEQ() throws RecognitionException {
        try {
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:234:5: ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\\\"' | '\\'' | '\\\\' ) | UNICODE_ESC | OCTAL_ESC )
            int alt5=3;
            int LA5_0 = input.LA(1);

            if ( (LA5_0=='\\') ) {
                switch ( input.LA(2) ) {
                case '\"':
                case '\'':
                case '\\':
                case 'b':
                case 'f':
                case 'n':
                case 'r':
                case 't':
                    {
                    alt5=1;
                    }
                    break;
                case 'u':
                    {
                    alt5=2;
                    }
                    break;
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                    {
                    alt5=3;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 5, 1, input);

                    throw nvae;

                }

            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;

            }
            switch (alt5) {
                case 1 :
                    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:234:9: '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\\\"' | '\\'' | '\\\\' )
                    {
                    match('\\'); 

                    if ( input.LA(1)=='\"'||input.LA(1)=='\''||input.LA(1)=='\\'||input.LA(1)=='b'||input.LA(1)=='f'||input.LA(1)=='n'||input.LA(1)=='r'||input.LA(1)=='t' ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    }
                    break;
                case 2 :
                    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:235:9: UNICODE_ESC
                    {
                    mUNICODE_ESC(); 


                    }
                    break;
                case 3 :
                    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:236:9: OCTAL_ESC
                    {
                    mOCTAL_ESC(); 


                    }
                    break;

            }

        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "ESC_SEQ"

    // $ANTLR start "OCTAL_ESC"
    public final void mOCTAL_ESC() throws RecognitionException {
        try {
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:241:5: ( '\\\\' ( '0' .. '3' ) ( '0' .. '7' ) ( '0' .. '7' ) | '\\\\' ( '0' .. '7' ) ( '0' .. '7' ) | '\\\\' ( '0' .. '7' ) )
            int alt6=3;
            int LA6_0 = input.LA(1);

            if ( (LA6_0=='\\') ) {
                int LA6_1 = input.LA(2);

                if ( ((LA6_1 >= '0' && LA6_1 <= '3')) ) {
                    int LA6_2 = input.LA(3);

                    if ( ((LA6_2 >= '0' && LA6_2 <= '7')) ) {
                        int LA6_4 = input.LA(4);

                        if ( ((LA6_4 >= '0' && LA6_4 <= '7')) ) {
                            alt6=1;
                        }
                        else {
                            alt6=2;
                        }
                    }
                    else {
                        alt6=3;
                    }
                }
                else if ( ((LA6_1 >= '4' && LA6_1 <= '7')) ) {
                    int LA6_3 = input.LA(3);

                    if ( ((LA6_3 >= '0' && LA6_3 <= '7')) ) {
                        alt6=2;
                    }
                    else {
                        alt6=3;
                    }
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 6, 1, input);

                    throw nvae;

                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;

            }
            switch (alt6) {
                case 1 :
                    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:241:9: '\\\\' ( '0' .. '3' ) ( '0' .. '7' ) ( '0' .. '7' )
                    {
                    match('\\'); 

                    if ( (input.LA(1) >= '0' && input.LA(1) <= '3') ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    if ( (input.LA(1) >= '0' && input.LA(1) <= '7') ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    if ( (input.LA(1) >= '0' && input.LA(1) <= '7') ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    }
                    break;
                case 2 :
                    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:242:9: '\\\\' ( '0' .. '7' ) ( '0' .. '7' )
                    {
                    match('\\'); 

                    if ( (input.LA(1) >= '0' && input.LA(1) <= '7') ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    if ( (input.LA(1) >= '0' && input.LA(1) <= '7') ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    }
                    break;
                case 3 :
                    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:243:9: '\\\\' ( '0' .. '7' )
                    {
                    match('\\'); 

                    if ( (input.LA(1) >= '0' && input.LA(1) <= '7') ) {
                        input.consume();
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;
                    }


                    }
                    break;

            }

        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "OCTAL_ESC"

    // $ANTLR start "UNICODE_ESC"
    public final void mUNICODE_ESC() throws RecognitionException {
        try {
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:248:5: ( '\\\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT )
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:248:9: '\\\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
            {
            match('\\'); 

            match('u'); 

            mHEX_DIGIT(); 


            mHEX_DIGIT(); 


            mHEX_DIGIT(); 


            mHEX_DIGIT(); 


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "UNICODE_ESC"

    public void mTokens() throws RecognitionException {
        // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:1:8: ( T__13 | T__14 | T__15 | T__16 | T__17 | T__18 | T__19 | T__20 | T__21 | T__22 | T__23 | T__24 | T__25 | ID | INT | WS | STRING | CHAR )
        int alt7=18;
        switch ( input.LA(1) ) {
        case '#':
            {
            alt7=1;
            }
            break;
        case ',':
            {
            alt7=2;
            }
            break;
        case ':':
            {
            alt7=3;
            }
            break;
        case ';':
            {
            alt7=4;
            }
            break;
        case '[':
            {
            alt7=5;
            }
            break;
        case ']':
            {
            alt7=6;
            }
            break;
        case 'b':
            {
            int LA7_7 = input.LA(2);

            if ( (LA7_7=='u') ) {
                int LA7_19 = input.LA(3);

                if ( (LA7_19=='t') ) {
                    int LA7_26 = input.LA(4);

                    if ( (LA7_26==' ') ) {
                        alt7=7;
                    }
                    else {
                        alt7=14;
                    }
                }
                else {
                    alt7=14;
                }
            }
            else {
                alt7=14;
            }
            }
            break;
        case 'c':
            {
            int LA7_8 = input.LA(2);

            if ( (LA7_8=='m') ) {
                int LA7_20 = input.LA(3);

                if ( (LA7_20=='d') ) {
                    int LA7_27 = input.LA(4);

                    if ( (LA7_27=='-'||(LA7_27 >= '0' && LA7_27 <= '9')||(LA7_27 >= 'A' && LA7_27 <= 'Z')||LA7_27=='_'||(LA7_27 >= 'a' && LA7_27 <= 'z')) ) {
                        alt7=14;
                    }
                    else {
                        alt7=8;
                    }
                }
                else {
                    alt7=14;
                }
            }
            else {
                alt7=14;
            }
            }
            break;
        case 'f':
            {
            int LA7_9 = input.LA(2);

            if ( (LA7_9=='o') ) {
                int LA7_21 = input.LA(3);

                if ( (LA7_21=='r') ) {
                    int LA7_28 = input.LA(4);

                    if ( (LA7_28=='-'||(LA7_28 >= '0' && LA7_28 <= '9')||(LA7_28 >= 'A' && LA7_28 <= 'Z')||LA7_28=='_'||(LA7_28 >= 'a' && LA7_28 <= 'z')) ) {
                        alt7=14;
                    }
                    else {
                        alt7=9;
                    }
                }
                else {
                    alt7=14;
                }
            }
            else {
                alt7=14;
            }
            }
            break;
        case 'g':
            {
            int LA7_10 = input.LA(2);

            if ( (LA7_10=='i') ) {
                int LA7_22 = input.LA(3);

                if ( (LA7_22=='t') ) {
                    int LA7_29 = input.LA(4);

                    if ( (LA7_29=='-'||(LA7_29 >= '0' && LA7_29 <= '9')||(LA7_29 >= 'A' && LA7_29 <= 'Z')||LA7_29=='_'||(LA7_29 >= 'a' && LA7_29 <= 'z')) ) {
                        alt7=14;
                    }
                    else {
                        alt7=10;
                    }
                }
                else {
                    alt7=14;
                }
            }
            else {
                alt7=14;
            }
            }
            break;
        case 'p':
            {
            int LA7_11 = input.LA(2);

            if ( (LA7_11=='r') ) {
                int LA7_23 = input.LA(3);

                if ( (LA7_23=='e') ) {
                    int LA7_30 = input.LA(4);

                    if ( (LA7_30=='d') ) {
                        int LA7_37 = input.LA(5);

                        if ( (LA7_37=='-'||(LA7_37 >= '0' && LA7_37 <= '9')||(LA7_37 >= 'A' && LA7_37 <= 'Z')||LA7_37=='_'||(LA7_37 >= 'a' && LA7_37 <= 'z')) ) {
                            alt7=14;
                        }
                        else {
                            alt7=11;
                        }
                    }
                    else {
                        alt7=14;
                    }
                }
                else {
                    alt7=14;
                }
            }
            else {
                alt7=14;
            }
            }
            break;
        case 'r':
            {
            int LA7_12 = input.LA(2);

            if ( (LA7_12=='u') ) {
                int LA7_24 = input.LA(3);

                if ( (LA7_24=='n') ) {
                    int LA7_31 = input.LA(4);

                    if ( (LA7_31=='s') ) {
                        int LA7_38 = input.LA(5);

                        if ( (LA7_38=='-'||(LA7_38 >= '0' && LA7_38 <= '9')||(LA7_38 >= 'A' && LA7_38 <= 'Z')||LA7_38=='_'||(LA7_38 >= 'a' && LA7_38 <= 'z')) ) {
                            alt7=14;
                        }
                        else {
                            alt7=12;
                        }
                    }
                    else {
                        alt7=14;
                    }
                }
                else {
                    alt7=14;
                }
            }
            else {
                alt7=14;
            }
            }
            break;
        case 's':
            {
            int LA7_13 = input.LA(2);

            if ( (LA7_13=='c') ) {
                int LA7_25 = input.LA(3);

                if ( (LA7_25=='o') ) {
                    int LA7_32 = input.LA(4);

                    if ( (LA7_32=='p') ) {
                        int LA7_39 = input.LA(5);

                        if ( (LA7_39=='e') ) {
                            int LA7_42 = input.LA(6);

                            if ( (LA7_42=='-'||(LA7_42 >= '0' && LA7_42 <= '9')||(LA7_42 >= 'A' && LA7_42 <= 'Z')||LA7_42=='_'||(LA7_42 >= 'a' && LA7_42 <= 'z')) ) {
                                alt7=14;
                            }
                            else {
                                alt7=13;
                            }
                        }
                        else {
                            alt7=14;
                        }
                    }
                    else {
                        alt7=14;
                    }
                }
                else {
                    alt7=14;
                }
            }
            else {
                alt7=14;
            }
            }
            break;
        case '-':
        case 'A':
        case 'B':
        case 'C':
        case 'D':
        case 'E':
        case 'F':
        case 'G':
        case 'H':
        case 'I':
        case 'J':
        case 'K':
        case 'L':
        case 'M':
        case 'N':
        case 'O':
        case 'P':
        case 'Q':
        case 'R':
        case 'S':
        case 'T':
        case 'U':
        case 'V':
        case 'W':
        case 'X':
        case 'Y':
        case 'Z':
        case '_':
        case 'a':
        case 'd':
        case 'e':
        case 'h':
        case 'i':
        case 'j':
        case 'k':
        case 'l':
        case 'm':
        case 'n':
        case 'o':
        case 'q':
        case 't':
        case 'u':
        case 'v':
        case 'w':
        case 'x':
        case 'y':
        case 'z':
            {
            alt7=14;
            }
            break;
        case '0':
        case '1':
        case '2':
        case '3':
        case '4':
        case '5':
        case '6':
        case '7':
        case '8':
        case '9':
            {
            alt7=15;
            }
            break;
        case '\t':
        case '\n':
        case '\r':
        case ' ':
            {
            alt7=16;
            }
            break;
        case '\"':
            {
            alt7=17;
            }
            break;
        case '\'':
            {
            alt7=18;
            }
            break;
        default:
            NoViableAltException nvae =
                new NoViableAltException("", 7, 0, input);

            throw nvae;

        }

        switch (alt7) {
            case 1 :
                // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:1:10: T__13
                {
                mT__13(); 


                }
                break;
            case 2 :
                // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:1:16: T__14
                {
                mT__14(); 


                }
                break;
            case 3 :
                // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:1:22: T__15
                {
                mT__15(); 


                }
                break;
            case 4 :
                // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:1:28: T__16
                {
                mT__16(); 


                }
                break;
            case 5 :
                // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:1:34: T__17
                {
                mT__17(); 


                }
                break;
            case 6 :
                // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:1:40: T__18
                {
                mT__18(); 


                }
                break;
            case 7 :
                // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:1:46: T__19
                {
                mT__19(); 


                }
                break;
            case 8 :
                // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:1:52: T__20
                {
                mT__20(); 


                }
                break;
            case 9 :
                // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:1:58: T__21
                {
                mT__21(); 


                }
                break;
            case 10 :
                // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:1:64: T__22
                {
                mT__22(); 


                }
                break;
            case 11 :
                // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:1:70: T__23
                {
                mT__23(); 


                }
                break;
            case 12 :
                // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:1:76: T__24
                {
                mT__24(); 


                }
                break;
            case 13 :
                // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:1:82: T__25
                {
                mT__25(); 


                }
                break;
            case 14 :
                // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:1:88: ID
                {
                mID(); 


                }
                break;
            case 15 :
                // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:1:91: INT
                {
                mINT(); 


                }
                break;
            case 16 :
                // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:1:95: WS
                {
                mWS(); 


                }
                break;
            case 17 :
                // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:1:98: STRING
                {
                mSTRING(); 


                }
                break;
            case 18 :
                // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:1:105: CHAR
                {
                mCHAR(); 


                }
                break;

        }

    }


 

}