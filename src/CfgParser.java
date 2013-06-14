// $ANTLR 3.4 /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g 2013-06-14 16:03:50


import java.util.ArrayList;
import java.util.HashMap;
import java.lang.StringBuilder;



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class CfgParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "CHAR", "ESC_SEQ", "HEX_DIGIT", "ID", "INT", "OCTAL_ESC", "STRING", "UNICODE_ESC", "WS", "'#'", "'('", "')'", "','", "':'", "';'", "'['", "']'", "'but exactly'", "'cmd'", "'errors'", "'for'", "'git'", "'pred'", "'runs'", "'scope'"
    };

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
    public static final int T__26=26;
    public static final int T__27=27;
    public static final int T__28=28;
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
    public Parser[] getDelegates() {
        return new Parser[] {};
    }

    // delegators


    public CfgParser(TokenStream input) {
        this(input, new RecognizerSharedState());
    }
    public CfgParser(TokenStream input, RecognizerSharedState state) {
        super(input, state);
    }

    public String[] getTokenNames() { return CfgParser.tokenNames; }
    public String getGrammarFileName() { return "/home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g"; }


    public static class cfg_return extends ParserRuleReturnScope {
        public ArrayList<HashMap<String,String>> vars;
        public ArrayList<ArrayList<String>> args;
        public ArrayList<ArrayList<String>> opts;
        public ArrayList<String> preds;
        public ArrayList<String> scopes;
        public ArrayList<String> cmds;
        public int n_runs;
        public int n_comands;
        public ArrayList<ArrayList<String>> errors;
    };


    // $ANTLR start "cfg"
    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:14:1: cfg returns [ArrayList<HashMap<String,String>> vars,ArrayList<ArrayList<String>> args ,ArrayList<ArrayList<String>> opts, ArrayList<String> preds,ArrayList<String> scopes, ArrayList<String> cmds ,int n_runs,int n_comands,ArrayList<ArrayList<String>> errors] : a= comand ( ';' b= comand )* r= runs ;
    public final CfgParser.cfg_return cfg() throws RecognitionException {
        CfgParser.cfg_return retval = new CfgParser.cfg_return();
        retval.start = input.LT(1);


        CfgParser.comand_return a =null;

        CfgParser.comand_return b =null;

        int r =0;



        ArrayList<HashMap<String,String>> vars = new ArrayList<HashMap<String,String>>();
        ArrayList<ArrayList<String>> args = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>> opts = new ArrayList<ArrayList<String>>();
        ArrayList<String> preds = new ArrayList<String>();
        ArrayList<String> scopes = new ArrayList<String>();
        ArrayList<String> cmds = new ArrayList<String>();
        ArrayList<ArrayList<String>> errors = new ArrayList<ArrayList<String>>();
        int n_comands = 0;

        try {
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:25:2: (a= comand ( ';' b= comand )* r= runs )
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:25:4: a= comand ( ';' b= comand )* r= runs
            {
            pushFollow(FOLLOW_comand_in_cfg28);
            a=comand();

            state._fsp--;



            	vars.add((a!=null?a.vars:null));
            	args.add((a!=null?a.args:null));
            	opts.add((a!=null?a.opts:null));
            	preds.add((a!=null?a.pred:null));
            	scopes.add((a!=null?a.scope:null));
            	cmds.add((a!=null?a.cmd:null));
            	errors.add((a!=null?a.err:null));
            	n_comands++;
            	

            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:36:3: ( ';' b= comand )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==18) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:36:5: ';' b= comand
            	    {
            	    match(input,18,FOLLOW_18_in_cfg37); 

            	    pushFollow(FOLLOW_comand_in_cfg41);
            	    b=comand();

            	    state._fsp--;



            	    	 vars.add((b!=null?b.vars:null));
            	    	 args.add((b!=null?b.args:null));
            	    	 opts.add((b!=null?b.opts:null));
            	    	 preds.add((b!=null?b.pred:null));
            	    	 scopes.add((b!=null?b.scope:null));
            	    	 cmds.add((b!=null?b.cmd:null));
            	    	 errors.add((b!=null?b.err:null));
            	    	 n_comands++;
            	    	 

            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


            pushFollow(FOLLOW_runs_in_cfg51);
            r=runs();

            state._fsp--;



            	 	retval.vars = vars;
            	 	retval.args = args;
            	 	retval.opts = opts;
            	 	retval.preds = preds;
            	 	retval.scopes = scopes;
            	 	retval.cmds = cmds;
            	 	retval.n_runs = r;
            	 	retval.n_comands = n_comands;
            	 	retval.errors = errors;
            	
            	 

            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "cfg"


    public static class comand_return extends ParserRuleReturnScope {
        public HashMap<String,String> vars;
        public ArrayList<String> args;
        public ArrayList<String> opts;
        public String pred;
        public String scope;
        public String cmd;
        public ArrayList<String> err;
    };


    // $ANTLR start "comand"
    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:62:1: comand returns [HashMap<String,String> vars, ArrayList<String> args, ArrayList<String> opts, String pred,String scope, String cmd, ArrayList<String> err] : ( vars )? 'pred' pred 'scope' sp 'cmd' cmdgit ( errors )? ;
    public final CfgParser.comand_return comand() throws RecognitionException {
        CfgParser.comand_return retval = new CfgParser.comand_return();
        retval.start = input.LT(1);


        HashMap<String,String> vars1 =null;

        CfgParser.pred_return pred2 =null;

        CfgParser.cmdgit_return cmdgit3 =null;

        String sp4 =null;

        ArrayList<String> errors5 =null;


        try {
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:63:2: ( ( vars )? 'pred' pred 'scope' sp 'cmd' cmdgit ( errors )? )
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:63:4: ( vars )? 'pred' pred 'scope' sp 'cmd' cmdgit ( errors )?
            {
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:63:4: ( vars )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==13) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:63:4: vars
                    {
                    pushFollow(FOLLOW_vars_in_comand74);
                    vars1=vars();

                    state._fsp--;


                    }
                    break;

            }


            match(input,26,FOLLOW_26_in_comand77); 

            pushFollow(FOLLOW_pred_in_comand79);
            pred2=pred();

            state._fsp--;


            match(input,28,FOLLOW_28_in_comand82); 

            pushFollow(FOLLOW_sp_in_comand85);
            sp4=sp();

            state._fsp--;


            match(input,22,FOLLOW_22_in_comand87); 

            pushFollow(FOLLOW_cmdgit_in_comand90);
            cmdgit3=cmdgit();

            state._fsp--;


            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:63:51: ( errors )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==23) ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:63:51: errors
                    {
                    pushFollow(FOLLOW_errors_in_comand94);
                    errors5=errors();

                    state._fsp--;


                    }
                    break;

            }



            	 	retval.vars = vars1;
            		retval.args = (pred2!=null?pred2.args:null);
            	 	retval.opts = (cmdgit3!=null?cmdgit3.opts:null);
            	  	retval.pred = (pred2!=null?pred2.name:null);
            	  	retval.scope = sp4;
            		retval.cmd = (cmdgit3!=null?cmdgit3.cmd:null);
            		retval.err = errors5;
            	 

            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "comand"



    // $ANTLR start "errors"
    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:75:1: errors returns [ArrayList<String> e_rror] : 'errors' '(' l= list_errors ')' ;
    public final ArrayList<String> errors() throws RecognitionException {
        ArrayList<String> e_rror = null;


        ArrayList<String> l =null;


        try {
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:76:3: ( 'errors' '(' l= list_errors ')' )
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:76:4: 'errors' '(' l= list_errors ')'
            {
            match(input,23,FOLLOW_23_in_errors115); 

            match(input,14,FOLLOW_14_in_errors117); 

            pushFollow(FOLLOW_list_errors_in_errors121);
            l=list_errors();

            state._fsp--;


            match(input,15,FOLLOW_15_in_errors123); 

            e_rror = l;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return e_rror;
    }
    // $ANTLR end "errors"



    // $ANTLR start "list_errors"
    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:80:1: list_errors returns [ArrayList<String> e_rror] : a= error ( ',' b= error )* ;
    public final ArrayList<String> list_errors() throws RecognitionException {
        ArrayList<String> e_rror = null;


        String a =null;

        String b =null;



        ArrayList<String>  err = new ArrayList<String>();

        try {
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:84:2: (a= error ( ',' b= error )* )
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:84:3: a= error ( ',' b= error )*
            {
            pushFollow(FOLLOW_error_in_list_errors147);
            a=error();

            state._fsp--;


            err.add(a);

            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:84:27: ( ',' b= error )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0==16) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:84:28: ',' b= error
            	    {
            	    match(input,16,FOLLOW_16_in_list_errors151); 

            	    pushFollow(FOLLOW_error_in_list_errors155);
            	    b=error();

            	    state._fsp--;


            	    err.add(b);

            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);


            e_rror = err;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return e_rror;
    }
    // $ANTLR end "list_errors"



    // $ANTLR start "error"
    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:88:1: error returns [String e] : s= STRING ;
    public final String error() throws RecognitionException {
        String e = null;


        Token s=null;

        try {
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:89:3: (s= STRING )
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:89:4: s= STRING
            {
            s=(Token)match(input,STRING,FOLLOW_STRING_in_error177); 


            	 e = (s!=null?s.getText():null);
            	

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return e;
    }
    // $ANTLR end "error"



    // $ANTLR start "runs"
    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:94:1: runs returns [int n_runs] : 'runs' i= INT ;
    public final int runs() throws RecognitionException {
        int n_runs = 0;


        Token i=null;

        try {
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:95:2: ( 'runs' i= INT )
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:95:4: 'runs' i= INT
            {
            match(input,27,FOLLOW_27_in_runs197); 

            i=(Token)match(input,INT,FOLLOW_INT_in_runs201); 


            	 n_runs = Integer.parseInt((i!=null?i.getText():null));
            	

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return n_runs;
    }
    // $ANTLR end "runs"



    // $ANTLR start "vars"
    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:101:1: vars returns [HashMap<String,String> a_vars] : a= var (b= var )* ;
    public final HashMap<String,String> vars() throws RecognitionException {
        HashMap<String,String> a_vars = null;


        CfgParser.var_return a =null;

        CfgParser.var_return b =null;



        HashMap<String,String> vars = new HashMap<String,String>();

        try {
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:105:2: (a= var (b= var )* )
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:105:4: a= var (b= var )*
            {
            pushFollow(FOLLOW_var_in_vars226);
            a=var();

            state._fsp--;


            vars.put((a!=null?a.arg:null),(a!=null?a.type:null));

            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:105:39: (b= var )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0==13) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:105:40: b= var
            	    {
            	    pushFollow(FOLLOW_var_in_vars234);
            	    b=var();

            	    state._fsp--;


            	    vars.put((b!=null?b.arg:null),(b!=null?b.type:null));

            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);


             a_vars = vars;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return a_vars;
    }
    // $ANTLR end "vars"


    public static class pred_return extends ParserRuleReturnScope {
        public String name;
        public ArrayList<String> args;
    };


    // $ANTLR start "pred"
    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:109:1: pred returns [String name, ArrayList<String> args] : n= name a= arguments ;
    public final CfgParser.pred_return pred() throws RecognitionException {
        CfgParser.pred_return retval = new CfgParser.pred_return();
        retval.start = input.LT(1);


        CfgParser.name_return n =null;

        ArrayList<String> a =null;


        try {
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:110:2: (n= name a= arguments )
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:110:4: n= name a= arguments
            {
            pushFollow(FOLLOW_name_in_pred259);
            n=name();

            state._fsp--;


            pushFollow(FOLLOW_arguments_in_pred263);
            a=arguments();

            state._fsp--;



            		  retval.args = a;
            		  retval.name = (n!=null?input.toString(n.start,n.stop):null);
            		

            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "pred"



    // $ANTLR start "arguments"
    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:118:1: arguments returns [ArrayList<String> args] : '[' (g= args )? ']' ;
    public final ArrayList<String> arguments() throws RecognitionException {
        ArrayList<String> args = null;


        ArrayList<String> g =null;


        try {
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:119:2: ( '[' (g= args )? ']' )
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:119:3: '[' (g= args )? ']'
            {
            match(input,19,FOLLOW_19_in_arguments283); 

            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:119:8: (g= args )?
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==13) ) {
                alt6=1;
            }
            switch (alt6) {
                case 1 :
                    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:119:8: g= args
                    {
                    pushFollow(FOLLOW_args_in_arguments287);
                    g=args();

                    state._fsp--;


                    }
                    break;

            }


            match(input,20,FOLLOW_20_in_arguments290); 


            	  args = g;
            	

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return args;
    }
    // $ANTLR end "arguments"



    // $ANTLR start "args"
    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:125:1: args returns [ArrayList<String> n_ag] : a= arg[true] ( ',' b= arg[true] )* ;
    public final ArrayList<String> args() throws RecognitionException {
        ArrayList<String> n_ag = null;


        String a =null;

        String b =null;



        ArrayList<String> args = new ArrayList<String>();

        try {
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:129:2: (a= arg[true] ( ',' b= arg[true] )* )
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:129:3: a= arg[true] ( ',' b= arg[true] )*
            {
            pushFollow(FOLLOW_arg_in_args313);
            a=arg(true);

            state._fsp--;


             args.add(a);

            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:129:35: ( ',' b= arg[true] )*
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( (LA7_0==16) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:129:36: ',' b= arg[true]
            	    {
            	    match(input,16,FOLLOW_16_in_args319); 

            	    pushFollow(FOLLOW_arg_in_args323);
            	    b=arg(true);

            	    state._fsp--;


            	     args.add(b);

            	    }
            	    break;

            	default :
            	    break loop7;
                }
            } while (true);



            	 n_ag = args;
            	

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return n_ag;
    }
    // $ANTLR end "args"



    // $ANTLR start "sp"
    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:136:1: sp returns [String sp] : ( 'for' i= INT sigs | 'for' j= INT 'but exactly' sigs );
    public final String sp() throws RecognitionException {
        String sp = null;


        Token i=null;
        Token j=null;
        ArrayList<String> sigs6 =null;

        ArrayList<String> sigs7 =null;



        StringBuilder scope = new StringBuilder();

        try {
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:140:2: ( 'for' i= INT sigs | 'for' j= INT 'but exactly' sigs )
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==24) ) {
                int LA8_1 = input.LA(2);

                if ( (LA8_1==INT) ) {
                    int LA8_2 = input.LA(3);

                    if ( (LA8_2==21) ) {
                        alt8=2;
                    }
                    else if ( (LA8_2==INT) ) {
                        alt8=1;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 8, 2, input);

                        throw nvae;

                    }
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 8, 1, input);

                    throw nvae;

                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 8, 0, input);

                throw nvae;

            }
            switch (alt8) {
                case 1 :
                    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:140:4: 'for' i= INT sigs
                    {
                    match(input,24,FOLLOW_24_in_sp352); 

                    i=(Token)match(input,INT,FOLLOW_INT_in_sp357); 

                    pushFollow(FOLLOW_sigs_in_sp360);
                    sigs6=sigs();

                    state._fsp--;



                    	 scope.append("for  " + (i!=null?i.getText():null)+ " ");
                    	 for (String sig : sigs6)
                    	  {
                    	   scope.append(sig +" ");  	
                    	  }
                    	  sp = scope.toString();
                    	

                    }
                    break;
                case 2 :
                    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:149:4: 'for' j= INT 'but exactly' sigs
                    {
                    match(input,24,FOLLOW_24_in_sp368); 

                    j=(Token)match(input,INT,FOLLOW_INT_in_sp372); 

                    match(input,21,FOLLOW_21_in_sp375); 

                    pushFollow(FOLLOW_sigs_in_sp377);
                    sigs7=sigs();

                    state._fsp--;



                    	 scope.append("for "  +(j!=null?j.getText():null)+ " but exactly ");
                    	 for (String sig : sigs7)
                    	  {
                    	   scope.append(sig +" ");  	
                    	  }
                    	  sp = scope.toString();
                    	

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return sp;
    }
    // $ANTLR end "sp"



    // $ANTLR start "sigs"
    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:160:1: sigs returns [ArrayList<String> n_sigs] : a= sig ( ',' b= sig )* ;
    public final ArrayList<String> sigs() throws RecognitionException {
        ArrayList<String> n_sigs = null;


        String a =null;

        String b =null;



        ArrayList<String> sigs = new ArrayList<String>();

        try {
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:164:2: (a= sig ( ',' b= sig )* )
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:164:4: a= sig ( ',' b= sig )*
            {
            pushFollow(FOLLOW_sig_in_sigs403);
            a=sig();

            state._fsp--;


             sigs.add(a);

            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:164:30: ( ',' b= sig )*
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( (LA9_0==16) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:164:31: ',' b= sig
            	    {
            	    match(input,16,FOLLOW_16_in_sigs407); 

            	    pushFollow(FOLLOW_sig_in_sigs411);
            	    b=sig();

            	    state._fsp--;


            	     sigs.add(b);

            	    }
            	    break;

            	default :
            	    break loop9;
                }
            } while (true);



            	 n_sigs = sigs;
            	

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return n_sigs;
    }
    // $ANTLR end "sigs"



    // $ANTLR start "sig"
    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:170:1: sig returns [String sig] : i= INT s= ID ;
    public final String sig() throws RecognitionException {
        String sig = null;


        Token i=null;
        Token s=null;

        try {
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:171:2: (i= INT s= ID )
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:171:3: i= INT s= ID
            {
            i=(Token)match(input,INT,FOLLOW_INT_in_sig436); 

            s=(Token)match(input,ID,FOLLOW_ID_in_sig441); 


            	 sig = (i!=null?i.getText():null) +" " + (s!=null?s.getText():null);
            	

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return sig;
    }
    // $ANTLR end "sig"


    public static class var_return extends ParserRuleReturnScope {
        public String arg;
        public String type;
    };


    // $ANTLR start "var"
    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:177:1: var returns [String arg, String type] : a= arg[false] ':' t= type ;
    public final CfgParser.var_return var() throws RecognitionException {
        CfgParser.var_return retval = new CfgParser.var_return();
        retval.start = input.LT(1);


        String a =null;

        CfgParser.type_return t =null;


        try {
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:178:2: (a= arg[false] ':' t= type )
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:178:4: a= arg[false] ':' t= type
            {
            pushFollow(FOLLOW_arg_in_var461);
            a=arg(false);

            state._fsp--;


            match(input,17,FOLLOW_17_in_var464); 

            pushFollow(FOLLOW_type_in_var468);
            t=type();

            state._fsp--;



            	 retval.arg = a;
            	 retval.type = (t!=null?input.toString(t.start,t.stop):null);
            	

            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "var"


    public static class type_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "type"
    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:185:1: type : ID ;
    public final CfgParser.type_return type() throws RecognitionException {
        CfgParser.type_return retval = new CfgParser.type_return();
        retval.start = input.LT(1);


        try {
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:186:2: ( ID )
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:186:3: ID
            {
            match(input,ID,FOLLOW_ID_in_type483); 

            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "type"


    public static class cmdgit_return extends ParserRuleReturnScope {
        public String pg;
        public String cmd;
        public ArrayList<String> opts;
    };


    // $ANTLR start "cmdgit"
    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:189:1: cmdgit returns [String pg,String cmd,ArrayList<String> opts] : 'git' name opts ;
    public final CfgParser.cmdgit_return cmdgit() throws RecognitionException {
        CfgParser.cmdgit_return retval = new CfgParser.cmdgit_return();
        retval.start = input.LT(1);


        CfgParser.name_return name8 =null;

        ArrayList<String> opts9 =null;


        try {
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:190:2: ( 'git' name opts )
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:190:3: 'git' name opts
            {
            match(input,25,FOLLOW_25_in_cmdgit498); 

            pushFollow(FOLLOW_name_in_cmdgit500);
            name8=name();

            state._fsp--;


            pushFollow(FOLLOW_opts_in_cmdgit502);
            opts9=opts();

            state._fsp--;



            	 retval.pg = "git";
            	 retval.cmd = (name8!=null?input.toString(name8.start,name8.stop):null);
            	 retval.opts = opts9;
            	

            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "cmdgit"



    // $ANTLR start "opts"
    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:198:1: opts returns [ArrayList<String> n_opts] : a= opt (b= opt )* ;
    public final ArrayList<String> opts() throws RecognitionException {
        ArrayList<String> n_opts = null;


        String a =null;

        String b =null;



        ArrayList<String> opts = new ArrayList<String>();

        try {
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:202:10: (a= opt (b= opt )* )
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:202:11: a= opt (b= opt )*
            {
            pushFollow(FOLLOW_opt_in_opts533);
            a=opt();

            state._fsp--;


            opts.add(a); 

            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:202:37: (b= opt )*
            loop10:
            do {
                int alt10=2;
                int LA10_0 = input.LA(1);

                if ( (LA10_0==ID||LA10_0==STRING||LA10_0==13) ) {
                    alt10=1;
                }


                switch (alt10) {
            	case 1 :
            	    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:202:38: b= opt
            	    {
            	    pushFollow(FOLLOW_opt_in_opts540);
            	    b=opt();

            	    state._fsp--;


            	    opts.add(b);

            	    }
            	    break;

            	default :
            	    break loop10;
                }
            } while (true);



            	 n_opts = opts;
            	

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return n_opts;
    }
    // $ANTLR end "opts"



    // $ANTLR start "opt"
    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:208:1: opt returns [String op] : (i= ID | arg[false] |s= STRING );
    public final String opt() throws RecognitionException {
        String op = null;


        Token i=null;
        Token s=null;
        String arg10 =null;


        try {
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:209:2: (i= ID | arg[false] |s= STRING )
            int alt11=3;
            switch ( input.LA(1) ) {
            case ID:
                {
                alt11=1;
                }
                break;
            case 13:
                {
                alt11=2;
                }
                break;
            case STRING:
                {
                alt11=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 11, 0, input);

                throw nvae;

            }

            switch (alt11) {
                case 1 :
                    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:209:4: i= ID
                    {
                    i=(Token)match(input,ID,FOLLOW_ID_in_opt567); 


                    	op = (i!=null?i.getText():null);
                    	

                    }
                    break;
                case 2 :
                    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:213:3: arg[false]
                    {
                    pushFollow(FOLLOW_arg_in_opt575);
                    arg10=arg(false);

                    state._fsp--;



                    	op = arg10;
                    	

                    }
                    break;
                case 3 :
                    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:217:4: s= STRING
                    {
                    s=(Token)match(input,STRING,FOLLOW_STRING_in_opt586); 


                    	op = (s!=null?s.getText():null);
                    	

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return op;
    }
    // $ANTLR end "opt"


    public static class name_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "name"
    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:224:1: name : ID ;
    public final CfgParser.name_return name() throws RecognitionException {
        CfgParser.name_return retval = new CfgParser.name_return();
        retval.start = input.LT(1);


        try {
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:225:2: ( ID )
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:225:4: ID
            {
            match(input,ID,FOLLOW_ID_in_name604); 

            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "name"



    // $ANTLR start "arg"
    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:228:1: arg[boolean flag] returns [String ag] : '#' i= ID ;
    public final String arg(boolean flag) throws RecognitionException {
        String ag = null;


        Token i=null;

        try {
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:229:2: ( '#' i= ID )
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:229:3: '#' i= ID
            {
            match(input,13,FOLLOW_13_in_arg621); 

            i=(Token)match(input,ID,FOLLOW_ID_in_arg624); 


            	if(flag)
            	  ag = (i!=null?i.getText():null);
            	  else ag = "#" +(i!=null?i.getText():null);
            	

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ag;
    }
    // $ANTLR end "arg"

    // Delegated rules


 

    public static final BitSet FOLLOW_comand_in_cfg28 = new BitSet(new long[]{0x0000000008040000L});
    public static final BitSet FOLLOW_18_in_cfg37 = new BitSet(new long[]{0x0000000004002000L});
    public static final BitSet FOLLOW_comand_in_cfg41 = new BitSet(new long[]{0x0000000008040000L});
    public static final BitSet FOLLOW_runs_in_cfg51 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_vars_in_comand74 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_26_in_comand77 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_pred_in_comand79 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_28_in_comand82 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_sp_in_comand85 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_22_in_comand87 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_cmdgit_in_comand90 = new BitSet(new long[]{0x0000000000800002L});
    public static final BitSet FOLLOW_errors_in_comand94 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_23_in_errors115 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_14_in_errors117 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_list_errors_in_errors121 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_15_in_errors123 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_error_in_list_errors147 = new BitSet(new long[]{0x0000000000010002L});
    public static final BitSet FOLLOW_16_in_list_errors151 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_error_in_list_errors155 = new BitSet(new long[]{0x0000000000010002L});
    public static final BitSet FOLLOW_STRING_in_error177 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_27_in_runs197 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_INT_in_runs201 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_var_in_vars226 = new BitSet(new long[]{0x0000000000002002L});
    public static final BitSet FOLLOW_var_in_vars234 = new BitSet(new long[]{0x0000000000002002L});
    public static final BitSet FOLLOW_name_in_pred259 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_arguments_in_pred263 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_19_in_arguments283 = new BitSet(new long[]{0x0000000000102000L});
    public static final BitSet FOLLOW_args_in_arguments287 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_20_in_arguments290 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_arg_in_args313 = new BitSet(new long[]{0x0000000000010002L});
    public static final BitSet FOLLOW_16_in_args319 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_arg_in_args323 = new BitSet(new long[]{0x0000000000010002L});
    public static final BitSet FOLLOW_24_in_sp352 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_INT_in_sp357 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_sigs_in_sp360 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_24_in_sp368 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_INT_in_sp372 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_21_in_sp375 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_sigs_in_sp377 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_sig_in_sigs403 = new BitSet(new long[]{0x0000000000010002L});
    public static final BitSet FOLLOW_16_in_sigs407 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_sig_in_sigs411 = new BitSet(new long[]{0x0000000000010002L});
    public static final BitSet FOLLOW_INT_in_sig436 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_ID_in_sig441 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_arg_in_var461 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_17_in_var464 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_type_in_var468 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_type483 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_25_in_cmdgit498 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_name_in_cmdgit500 = new BitSet(new long[]{0x0000000000002480L});
    public static final BitSet FOLLOW_opts_in_cmdgit502 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_opt_in_opts533 = new BitSet(new long[]{0x0000000000002482L});
    public static final BitSet FOLLOW_opt_in_opts540 = new BitSet(new long[]{0x0000000000002482L});
    public static final BitSet FOLLOW_ID_in_opt567 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_arg_in_opt575 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_in_opt586 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_name604 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_13_in_arg621 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_ID_in_arg624 = new BitSet(new long[]{0x0000000000000002L});

}