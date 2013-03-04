// $ANTLR 3.4 /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g 2013-03-04 01:35:14


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
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "CHAR", "ESC_SEQ", "HEX_DIGIT", "ID", "INT", "OCTAL_ESC", "STRING", "UNICODE_ESC", "WS", "'#'", "','", "':'", "';'", "'['", "']'", "'cmd'", "'exactly'", "'for'", "'git'", "'pred'", "'runs'", "'s'", "'s2'", "'scope'"
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
    };


    // $ANTLR start "cfg"
    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:14:1: cfg returns [ArrayList<HashMap<String,String>> vars,ArrayList<ArrayList<String>> args ,ArrayList<ArrayList<String>> opts, ArrayList<String> preds,ArrayList<String> scopes, ArrayList<String> cmds ,int n_runs,int n_comands] : a= comand ( ';' b= comand )* r= runs ;
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
        int n_comands = 0;

        try {
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:24:2: (a= comand ( ';' b= comand )* r= runs )
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:24:4: a= comand ( ';' b= comand )* r= runs
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
            	n_comands++;
            	

            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:34:3: ( ';' b= comand )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==16) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:34:5: ';' b= comand
            	    {
            	    match(input,16,FOLLOW_16_in_cfg37); 

            	    pushFollow(FOLLOW_comand_in_cfg41);
            	    b=comand();

            	    state._fsp--;



            	    	 vars.add((b!=null?b.vars:null));
            	    	 args.add((b!=null?b.args:null));
            	    	 opts.add((b!=null?b.opts:null));
            	    	 preds.add((b!=null?b.pred:null));
            	    	 scopes.add((b!=null?b.scope:null));
            	    	 cmds.add((b!=null?b.cmd:null));
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
            	 	System.out.println(n_comands);
            	 	retval.n_comands = n_comands;
            	 

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
    };


    // $ANTLR start "comand"
    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:58:1: comand returns [HashMap<String,String> vars, ArrayList<String> args, ArrayList<String> opts, String pred,String scope, String cmd] : vars 'pred' pred 'scope' sp 'cmd' cmdgit ;
    public final CfgParser.comand_return comand() throws RecognitionException {
        CfgParser.comand_return retval = new CfgParser.comand_return();
        retval.start = input.LT(1);


        HashMap<String,String> vars1 =null;

        CfgParser.pred_return pred2 =null;

        CfgParser.cmdgit_return cmdgit3 =null;

        String sp4 =null;


        try {
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:59:2: ( vars 'pred' pred 'scope' sp 'cmd' cmdgit )
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:59:4: vars 'pred' pred 'scope' sp 'cmd' cmdgit
            {
            pushFollow(FOLLOW_vars_in_comand74);
            vars1=vars();

            state._fsp--;


            match(input,23,FOLLOW_23_in_comand76); 

            pushFollow(FOLLOW_pred_in_comand78);
            pred2=pred();

            state._fsp--;


            match(input,27,FOLLOW_27_in_comand81); 

            pushFollow(FOLLOW_sp_in_comand84);
            sp4=sp();

            state._fsp--;


            match(input,19,FOLLOW_19_in_comand86); 

            pushFollow(FOLLOW_cmdgit_in_comand89);
            cmdgit3=cmdgit();

            state._fsp--;



            	 	retval.vars = vars1;
            		retval.args = (pred2!=null?pred2.args:null);
            	 	retval.opts = (cmdgit3!=null?cmdgit3.opts:null);
            	  	retval.pred = (pred2!=null?pred2.name:null);
            	  	retval.scope = sp4;
            		retval.cmd = (cmdgit3!=null?cmdgit3.cmd:null);
            	
            	 

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



    // $ANTLR start "runs"
    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:71:1: runs returns [int n_runs] : 'runs' i= INT ;
    public final int runs() throws RecognitionException {
        int n_runs = 0;


        Token i=null;

        try {
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:72:2: ( 'runs' i= INT )
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:72:4: 'runs' i= INT
            {
            match(input,24,FOLLOW_24_in_runs111); 

            i=(Token)match(input,INT,FOLLOW_INT_in_runs115); 


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
    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:78:1: vars returns [HashMap<String,String> a_vars] : a= var (b= var )* ;
    public final HashMap<String,String> vars() throws RecognitionException {
        HashMap<String,String> a_vars = null;


        CfgParser.var_return a =null;

        CfgParser.var_return b =null;



        HashMap<String,String> vars = new HashMap<String,String>();

        try {
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:82:2: (a= var (b= var )* )
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:82:4: a= var (b= var )*
            {
            pushFollow(FOLLOW_var_in_vars140);
            a=var();

            state._fsp--;


            vars.put((a!=null?a.arg:null),(a!=null?a.type:null));

            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:82:39: (b= var )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0==13) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:82:40: b= var
            	    {
            	    pushFollow(FOLLOW_var_in_vars148);
            	    b=var();

            	    state._fsp--;


            	    vars.put((b!=null?b.arg:null),(b!=null?b.type:null));

            	    }
            	    break;

            	default :
            	    break loop2;
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
    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:86:1: pred returns [String name, ArrayList<String> args] : n= name a= arguments ;
    public final CfgParser.pred_return pred() throws RecognitionException {
        CfgParser.pred_return retval = new CfgParser.pred_return();
        retval.start = input.LT(1);


        CfgParser.name_return n =null;

        ArrayList<String> a =null;


        try {
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:87:2: (n= name a= arguments )
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:87:4: n= name a= arguments
            {
            pushFollow(FOLLOW_name_in_pred173);
            n=name();

            state._fsp--;


            pushFollow(FOLLOW_arguments_in_pred177);
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
    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:95:1: arguments returns [ArrayList<String> args] : '[' 's' ',' 's2' ',' g= args ']' ;
    public final ArrayList<String> arguments() throws RecognitionException {
        ArrayList<String> args = null;


        ArrayList<String> g =null;


        try {
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:96:2: ( '[' 's' ',' 's2' ',' g= args ']' )
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:96:3: '[' 's' ',' 's2' ',' g= args ']'
            {
            match(input,17,FOLLOW_17_in_arguments197); 

            match(input,25,FOLLOW_25_in_arguments199); 

            match(input,14,FOLLOW_14_in_arguments201); 

            match(input,26,FOLLOW_26_in_arguments203); 

            match(input,14,FOLLOW_14_in_arguments205); 

            pushFollow(FOLLOW_args_in_arguments209);
            g=args();

            state._fsp--;


            match(input,18,FOLLOW_18_in_arguments211); 


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
    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:102:1: args returns [ArrayList<String> n_ag] : a= arg ( ',' b= arg )* ;
    public final ArrayList<String> args() throws RecognitionException {
        ArrayList<String> n_ag = null;


        String a =null;

        String b =null;



        ArrayList<String> args = new ArrayList<String>();

        try {
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:106:2: (a= arg ( ',' b= arg )* )
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:106:3: a= arg ( ',' b= arg )*
            {
            pushFollow(FOLLOW_arg_in_args234);
            a=arg();

            state._fsp--;


             args.add(a);

            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:106:29: ( ',' b= arg )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==14) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:106:30: ',' b= arg
            	    {
            	    match(input,14,FOLLOW_14_in_args239); 

            	    pushFollow(FOLLOW_arg_in_args243);
            	    b=arg();

            	    state._fsp--;


            	     args.add(b);

            	    }
            	    break;

            	default :
            	    break loop3;
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
    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:113:1: sp returns [String sp] : ( 'for' sigs | 'for' 'exactly' sigs );
    public final String sp() throws RecognitionException {
        String sp = null;


        ArrayList<String> sigs5 =null;

        ArrayList<String> sigs6 =null;



        StringBuilder scope = new StringBuilder();

        try {
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:117:2: ( 'for' sigs | 'for' 'exactly' sigs )
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==21) ) {
                int LA4_1 = input.LA(2);

                if ( (LA4_1==20) ) {
                    alt4=2;
                }
                else if ( (LA4_1==INT) ) {
                    alt4=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 4, 1, input);

                    throw nvae;

                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                throw nvae;

            }
            switch (alt4) {
                case 1 :
                    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:117:4: 'for' sigs
                    {
                    match(input,21,FOLLOW_21_in_sp271); 

                    pushFollow(FOLLOW_sigs_in_sp273);
                    sigs5=sigs();

                    state._fsp--;



                    	 scope.append("for  ");
                    	 for (String sig : sigs5)
                    	  {
                    	   scope.append(sig +" ");  	
                    	  }
                    	  sp = scope.toString();
                    	

                    }
                    break;
                case 2 :
                    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:126:4: 'for' 'exactly' sigs
                    {
                    match(input,21,FOLLOW_21_in_sp281); 

                    match(input,20,FOLLOW_20_in_sp283); 

                    pushFollow(FOLLOW_sigs_in_sp285);
                    sigs6=sigs();

                    state._fsp--;



                    	 scope.append("for exactly ");
                    	 for (String sig : sigs6)
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
    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:137:1: sigs returns [ArrayList<String> n_sigs] : a= sig ( ',' b= sig )* ;
    public final ArrayList<String> sigs() throws RecognitionException {
        ArrayList<String> n_sigs = null;


        String a =null;

        String b =null;



        ArrayList<String> sigs = new ArrayList<String>();

        try {
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:141:2: (a= sig ( ',' b= sig )* )
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:141:4: a= sig ( ',' b= sig )*
            {
            pushFollow(FOLLOW_sig_in_sigs311);
            a=sig();

            state._fsp--;


             sigs.add(a);

            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:141:30: ( ',' b= sig )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( (LA5_0==14) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:141:31: ',' b= sig
            	    {
            	    match(input,14,FOLLOW_14_in_sigs315); 

            	    pushFollow(FOLLOW_sig_in_sigs319);
            	    b=sig();

            	    state._fsp--;


            	     sigs.add(b);

            	    }
            	    break;

            	default :
            	    break loop5;
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
    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:147:1: sig returns [String sig] : i= INT s= ID ;
    public final String sig() throws RecognitionException {
        String sig = null;


        Token i=null;
        Token s=null;

        try {
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:148:2: (i= INT s= ID )
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:148:3: i= INT s= ID
            {
            i=(Token)match(input,INT,FOLLOW_INT_in_sig344); 

            s=(Token)match(input,ID,FOLLOW_ID_in_sig349); 


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
    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:154:1: var returns [String arg, String type] : a= arg ':' t= type ;
    public final CfgParser.var_return var() throws RecognitionException {
        CfgParser.var_return retval = new CfgParser.var_return();
        retval.start = input.LT(1);


        String a =null;

        CfgParser.type_return t =null;


        try {
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:155:2: (a= arg ':' t= type )
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:155:4: a= arg ':' t= type
            {
            pushFollow(FOLLOW_arg_in_var369);
            a=arg();

            state._fsp--;


            match(input,15,FOLLOW_15_in_var371); 

            pushFollow(FOLLOW_type_in_var375);
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
    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:162:1: type : ID ;
    public final CfgParser.type_return type() throws RecognitionException {
        CfgParser.type_return retval = new CfgParser.type_return();
        retval.start = input.LT(1);


        try {
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:163:2: ( ID )
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:163:3: ID
            {
            match(input,ID,FOLLOW_ID_in_type390); 

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
    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:166:1: cmdgit returns [String pg,String cmd,ArrayList<String> opts] : 'git' name opts ;
    public final CfgParser.cmdgit_return cmdgit() throws RecognitionException {
        CfgParser.cmdgit_return retval = new CfgParser.cmdgit_return();
        retval.start = input.LT(1);


        CfgParser.name_return name7 =null;

        ArrayList<String> opts8 =null;


        try {
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:167:2: ( 'git' name opts )
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:167:3: 'git' name opts
            {
            match(input,22,FOLLOW_22_in_cmdgit405); 

            pushFollow(FOLLOW_name_in_cmdgit407);
            name7=name();

            state._fsp--;


            pushFollow(FOLLOW_opts_in_cmdgit409);
            opts8=opts();

            state._fsp--;



            	 retval.pg = "git";
            	 retval.cmd = (name7!=null?input.toString(name7.start,name7.stop):null);
            	 retval.opts = opts8;
            	

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
    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:175:1: opts returns [ArrayList<String> n_opts] : a= opt (b= opt )* ;
    public final ArrayList<String> opts() throws RecognitionException {
        ArrayList<String> n_opts = null;


        String a =null;

        String b =null;



        ArrayList<String> opts = new ArrayList<String>();

        try {
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:179:10: (a= opt (b= opt )* )
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:179:11: a= opt (b= opt )*
            {
            pushFollow(FOLLOW_opt_in_opts440);
            a=opt();

            state._fsp--;


            opts.add(a); 

            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:179:37: (b= opt )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0==17) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:179:38: b= opt
            	    {
            	    pushFollow(FOLLOW_opt_in_opts447);
            	    b=opt();

            	    state._fsp--;


            	    opts.add(b);

            	    }
            	    break;

            	default :
            	    break loop6;
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
    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:185:1: opt returns [String op] : '[' i= ID ']' ;
    public final String opt() throws RecognitionException {
        String op = null;


        Token i=null;

        try {
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:186:2: ( '[' i= ID ']' )
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:186:3: '[' i= ID ']'
            {
            match(input,17,FOLLOW_17_in_opt470); 

            i=(Token)match(input,ID,FOLLOW_ID_in_opt475); 

            match(input,18,FOLLOW_18_in_opt477); 


            	op = (i!=null?i.getText():null);
            	

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
    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:191:1: name : ID ;
    public final CfgParser.name_return name() throws RecognitionException {
        CfgParser.name_return retval = new CfgParser.name_return();
        retval.start = input.LT(1);


        try {
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:192:2: ( ID )
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:192:4: ID
            {
            match(input,ID,FOLLOW_ID_in_name491); 

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
    // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:195:1: arg returns [String ag] : '#' i= INT ;
    public final String arg() throws RecognitionException {
        String ag = null;


        Token i=null;

        try {
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:196:2: ( '#' i= INT )
            // /home/jpinheiro/Work/mfes/validAlloy/src/Cfg.g:196:3: '#' i= INT
            {
            match(input,13,FOLLOW_13_in_arg506); 

            i=(Token)match(input,INT,FOLLOW_INT_in_arg509); 


            	 ag = (i!=null?i.getText():null);
            	

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


 

    public static final BitSet FOLLOW_comand_in_cfg28 = new BitSet(new long[]{0x0000000001010000L});
    public static final BitSet FOLLOW_16_in_cfg37 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_comand_in_cfg41 = new BitSet(new long[]{0x0000000001010000L});
    public static final BitSet FOLLOW_runs_in_cfg51 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_vars_in_comand74 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_23_in_comand76 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_pred_in_comand78 = new BitSet(new long[]{0x0000000008000000L});
    public static final BitSet FOLLOW_27_in_comand81 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_sp_in_comand84 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_19_in_comand86 = new BitSet(new long[]{0x0000000000400000L});
    public static final BitSet FOLLOW_cmdgit_in_comand89 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_24_in_runs111 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_INT_in_runs115 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_var_in_vars140 = new BitSet(new long[]{0x0000000000002002L});
    public static final BitSet FOLLOW_var_in_vars148 = new BitSet(new long[]{0x0000000000002002L});
    public static final BitSet FOLLOW_name_in_pred173 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_arguments_in_pred177 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_17_in_arguments197 = new BitSet(new long[]{0x0000000002000000L});
    public static final BitSet FOLLOW_25_in_arguments199 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_14_in_arguments201 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_26_in_arguments203 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_14_in_arguments205 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_args_in_arguments209 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_18_in_arguments211 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_arg_in_args234 = new BitSet(new long[]{0x0000000000004002L});
    public static final BitSet FOLLOW_14_in_args239 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_arg_in_args243 = new BitSet(new long[]{0x0000000000004002L});
    public static final BitSet FOLLOW_21_in_sp271 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_sigs_in_sp273 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_21_in_sp281 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_20_in_sp283 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_sigs_in_sp285 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_sig_in_sigs311 = new BitSet(new long[]{0x0000000000004002L});
    public static final BitSet FOLLOW_14_in_sigs315 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_sig_in_sigs319 = new BitSet(new long[]{0x0000000000004002L});
    public static final BitSet FOLLOW_INT_in_sig344 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_ID_in_sig349 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_arg_in_var369 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_15_in_var371 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_type_in_var375 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_type390 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_22_in_cmdgit405 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_name_in_cmdgit407 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_opts_in_cmdgit409 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_opt_in_opts440 = new BitSet(new long[]{0x0000000000020002L});
    public static final BitSet FOLLOW_opt_in_opts447 = new BitSet(new long[]{0x0000000000020002L});
    public static final BitSet FOLLOW_17_in_opt470 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_ID_in_opt475 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_18_in_opt477 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_name491 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_13_in_arg506 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_INT_in_arg509 = new BitSet(new long[]{0x0000000000000002L});

}